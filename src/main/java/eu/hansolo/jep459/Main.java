package eu.hansolo.jep459;

public class Main {
    public static void main(String[] args) {
        Person p1 = new Person("Gerrit", "Grunwald", "MilkyWay 312", "0815", "Mos Isley");
        System.out.println(p1);
        System.out.println(p1.toJson());
    }
}
