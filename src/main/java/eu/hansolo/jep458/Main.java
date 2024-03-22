package eu.hansolo.jep458;

public class Main {
    // java --add-modules=jdk.incubator.vector Main.java HanSolo
    public static void main(String[] args) {
        String text = Helper.check(args);
        System.out.println("Hello "+ text);
    }
}
