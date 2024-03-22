package eu.hansolo.jep461;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;


public class Main {
    private static final Random RND            = new Random();
    private static final String POLYGON_POINTS = "0,0,0,10,-20,20,80,20,100,10,100,0,0,0";

    record Point(int x, int y) {}
    record Data(int index, long timestamp, double value) {
        @Override public String toString() { return STR."""
              {
                  "index":\{index},
                  "timestamp":\{timestamp},
                  "value":\{value}
                }""";
        }
    }
    record Spike(Data data, double delta) {}

    private static final double     amplitude  = 350;
    private static final List<Data> timeseries = new ArrayList<>();
    private static final double     threshold  = 100;
    static {
        long   time      = System.nanoTime();
        double lastNoise = 0;
        for (int i = 0 ; i < 2880 ; i++) {
            double x = i % 360;
            double y;
            double rnd = RND.nextDouble() * 100 - 50;
            if (System.nanoTime() - time > 500_000) {
                y    = 50 + Math.sin(Math.toRadians(x)) * amplitude + lastNoise + RND.nextDouble() * 100 + threshold;
                time = System.nanoTime();
            } else {
                y    = 50 + Math.sin(Math.toRadians(x)) * amplitude + rnd;
            }
            lastNoise = rnd;
            final Data   data = new Data(i, System.nanoTime(), y);
            timeseries.add(data);
        }
    }


    public Main() {
        // Fixed window gatherer
        fixedWindowGatherer().forEach(p -> System.out.println("x: " + p.x + ", y: " + p.y));

        // Sliding window gatherer
        slidingWindowGatherer();
    }


    public List<Point> fixedWindowGatherer() {
        System.out.println("\nTransform given String of polygon coordinates into points: " + POLYGON_POINTS);

        // Create a list of points from the String
        List<String>        numbers       = Arrays.stream(POLYGON_POINTS.split(","))
                                                  .toList();

        // Map Strings to Integer, create a fixed window of 2 and collect it to a List of List<Integer>
        List<List<Integer>> windows       = numbers.stream()
                                                   .mapToInt(n -> Integer.valueOf(n))
                                                   .boxed()
                                                   .gather(Gatherers.windowFixed(2))
                                                   .toList();
        // Map each List<Integer> to a List<Point>
        List<Point>         points        = windows.stream()
                                                   .map(p -> new Point(p.get(0), p.get(1)))
                                                   .toList();

        return points;
    }

    public void slidingWindowGatherer() {
        System.out.println("\nFind spikes in timeseries of " + timeseries.size() + " datapoints:");
        List<Spike> spikes = timeseries.stream()
                                       .gather(Gatherers.windowSliding(2))
                                       .filter(dataList -> (dataList.get(1).value - dataList.get(0).value) > threshold)
                                       .map(dataList -> new Spike(dataList.get(1), (dataList.get(1).value - dataList.get(0).value)))
                                       .toList();
        System.out.println("Spikes found: " + spikes.size());
        List<Long> nsDeltas = spikes.stream()
                                    .gather(Gatherers.windowSliding(2))
                                    .map(spikeList -> (spikeList.get(1).data.timestamp - spikeList.get(0).data.timestamp))
                                    .toList();

        double averageDelta = nsDeltas.stream().mapToDouble(Long::doubleValue).average().orElse(0.0) / 1_000_000;
        System.out.println("Average ms between spikes: " + averageDelta);
        spikes.forEach(spike -> System.out.println("index: " + spike.data.index + ", timestamp: " + spike.data.timestamp + ", value: " + spike.data.value + ", delta: " + spike.delta));
    }


    public static void main(String[] args) {
        new Main();
    }
}
