package com.example;

import java.io.PrintStream;

public class JavaClass {
    public static PrintStream Out = System.out;

    public static void main(String... args) {
        Out.println("Hello Java");

        demo1();

        end();
    }

    public static void demo1() {
        final int B = 1;
        Out.println(B);
        Out.println(B << 1);
        Out.println(B << 1 << 1);
        Out.println(B << 1 << 1 << 1);
        Out.println(B << 1 << 1 << 1 << 1);
        Out.println(B << 1 << 1 << 1 << 1 << 1);
        Out.println(B << 1 << 1 << 1 << 1 << 1 << 1);
    }

    public static void end() {
        Out.println("-----------------------------end----------------------------------");
    }
}
