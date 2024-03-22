package eu.hansolo.jep460;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Random;


public class Main {
    private static final Random               RND     = new Random();
    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

    Main() {
        addArrays();
        addArraysVectorized();

        int     arraySize   = 134217728;
        float[] a           = new float[arraySize];
        float[] b           = new float[arraySize];
        for (int i = 0 ; i < arraySize ; i++) {
            a[i] = RND.nextFloat(32);
            b[i] = RND.nextFloat(32);
        }

        scalar(arraySize, a, b);
        vectors(arraySize, a, b);
    }


    public void addArrays() {
        int[] a = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[] b = { 8, 7, 6, 5, 4, 3, 2, 1 };
        int[] c = new int[8];

        for (int i = 0 ; i < 8 ; i++) {
            c[i] = a[i] + b[i];
        }
    }

    public void addArraysVectorized() {
        int[] a = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[] b = { 8, 7, 6, 5, 4, 3, 2, 1 };
        int[] c = new int[8];

        IntVector vectorA = IntVector.fromArray(IntVector.SPECIES_PREFERRED, a, 0);
        IntVector vectorB = IntVector.fromArray(IntVector.SPECIES_PREFERRED, b, 0);
        IntVector vectorC = vectorA.add(vectorB);
        vectorC.intoArray(c, 0);
    }

    public void scalar(final int arraySize, final float[] a, final float[] b) {
        int lowerThan   = 0;
        int equal       = 0;
        int greaterThan = 0;

        long start = System.nanoTime();
        for (int i = 0; i < arraySize; i++) {
            if (a[i] == b[i]) {
                equal++;
            } else if (a[i] > b[i]) {
                greaterThan++;
            } else {
                lowerThan++;
            }
        }
        System.out.println("Scalar: " + arraySize + "  " + equal + "   " + lowerThan + "   " + greaterThan + " -> " + ((System.nanoTime() - start) / 1_000_000) + "ms");
    }

    public void vectors(final int arraySize, final float[] a, final float[] b) {
        int lowerThan   = 0;
        int equal       = 0;
        int greaterThan = 0;

        long start = System.nanoTime();
        for (int i = 0; i < arraySize; i += SPECIES.length()) {
            FloatVector vectorA          = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vectorB          = FloatVector.fromArray(SPECIES, b, i);
            int         lowerThanCounter = vectorA.lt(vectorB).trueCount();
            int         equalCounter     = vectorA.eq(vectorB).trueCount();
            equal       += equalCounter;
            lowerThan += lowerThanCounter;
            greaterThan += SPECIES.length() - lowerThanCounter - equalCounter;
        }
        System.out.println("Vector: " + arraySize + "  " + equal + "   " + lowerThan + "   " + greaterThan + " -> " + ((System.nanoTime() - start) / 1_000_000) + "ms");
    }


    public static void main(String[] args) {
        new Main();
    }
}
