package com.example;

import java.io.PrintStream;

public class JavaClass {
    static final PrintStream p = System.out;

    public static PrintStream Out = System.out;

    public static void main(String... args) {
        Out.println("Hello Java");

//        Out.println(2/3);
//        Out.println(2/(float)3);
//        Out.println((float)2/3);
//        Out.println(2f/3f);
//        Out.println(2/3f);
//        Out.println(2f/3);
//
//        demo1();
//

//        RsaClass.demo("15916015262");
//        RsaClass.demo("15916015262");
//        RsaClass.demo("15916015262");
//        RsaClass.demo("15916015262");
//        RsaClass.demo("15916015262");

//        end();

        TestClass testClass = new TestClass();
        Out.println(testClass.num);
        testClass(testClass);
        Out.println(testClass.num);
    }

    public static void testClass(final TestClass testClass) {
        testClass.num = 100;
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

    public static class TestClass{
        public int num;
    }
}
