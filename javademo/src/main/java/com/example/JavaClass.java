package com.example;

import com.rsa.RsaClass;

import java.io.PrintStream;

public class JavaClass {
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

        RsaClass.demo("15916015262");
        RsaClass.demo("15916015262");
        RsaClass.demo("15916015262");
        RsaClass.demo("15916015262");

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
