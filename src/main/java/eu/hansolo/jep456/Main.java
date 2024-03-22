package eu.hansolo.jep456;

import java.util.List;


public class Main {

    public Main() {

    }

    // Unused variable
    public static int countOld(List<String> names) {
        int total = 0;
        for (String name : names) {
            total++;
        }
        return total;
    }

    public static int countNew(List<String> names) {
        int total = 0;
        for (String _ : names) {
            total++;
        }
        return total;
    }


    // Unnamed pattern variable
    sealed abstract class Ball permits RedBall, BlueBall, GreenBall { }
    final class RedBall   extends Ball { }
    final class BlueBall  extends Ball { }
    final class GreenBall extends Ball { }

    public static void filterOld(Ball ball) {
        switch (ball) {
            case RedBall   red   -> process(ball);
            case GreenBall green -> process(ball);
            case BlueBall  blue  -> process(ball);
        }
    }

    public static void filterNew(Ball ball) {
        switch (ball) {
            case RedBall   _ -> process(ball);
            case GreenBall _ -> process(ball);
            case BlueBall  _ -> process(ball);
        }
    }

    public static void process(final Ball ball) {
        System.out.println("Ball: " + ball);
    }

    public static void main(String[] args) {
        new Main();
    }
}
