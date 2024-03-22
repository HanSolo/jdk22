package eu.hansolo.jep457;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.nio.file.Path;

import static java.lang.classfile.ClassFile.ACC_PUBLIC;
import static java.lang.classfile.ClassFile.ACC_STATIC;


public class Main {

    public Main() {
        try {
            writeHelloWorldClass();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHelloWorldClass() throws IOException {
        ClassFile helloWorldClass = ClassFile.of();
        helloWorldClass.buildTo(Path.of("HelloWorld.class"), ClassDesc.of("HelloWorld"), classBuilder -> {
            classBuilder.withMethodBody("main", MethodTypeDesc.ofDescriptor("([Ljava/lang/String;)V"), ACC_PUBLIC | ACC_STATIC, codeBuilder -> {
                codeBuilder.getstatic(ClassDesc.of("java.lang.System"), "out", ClassDesc.of("java.io.PrintStream"))
                .ldc("Hello World")
                .invokevirtual(ClassDesc.of("java.io.PrintStream"), "println", MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)V"))
                .return_();
            });
        });
    }


    public static void main(String[] args) {
        new Main();
    }
}
