package eu.hansolo.jep458;


public class Helper {
    public static final String check(final String[] args) {
        return args.length == 0 ? "World" : args[0];
    }
}
