package eu.hansolo.jep463;

public class Main {

    // java --source 22 --enable-preview --add-modules=jdk.incubator.vector Main.java

    public Main() {
        this(new String[]{});
        System.out.println("Empty Constructor");
    }
    public Main(String[] args) {
        System.out.println(STR."Hello \{ null == args || args.length == 0 ? "World" : args[0] }");
    }


    void main() {
        new Main(new String[]{"Han"});
        System.out.println("Called empty constructor");
    }
    void main(String[] args) {
        new Main(args);
        System.out.println("Called constructor with args");
    }
}
