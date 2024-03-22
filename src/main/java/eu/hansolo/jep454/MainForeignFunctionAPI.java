package eu.hansolo.jep454;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.nio.file.Path;


public class MainForeignFunctionAPI {

    public MainForeignFunctionAPI() {
        try (var arena = Arena.ofConfined()) {
            var lib                = SymbolLookup.libraryLookup(Path.of("/Users/hansolo/IntelliJ_Projects/jdk22/src/main/java/eu/hansolo/jep454/libadd.so"), arena);
            var linker             = Linker.nativeLinker();
            var functionDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT);
            var addFunc            = lib.find("add").get();
            var methodHandle       = linker.downcallHandle(addFunc, functionDescriptor);
            var sum                = methodHandle.invoke(1, 2);
            System.out.println("sum = " + sum);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        new MainForeignFunctionAPI();
    }
}
