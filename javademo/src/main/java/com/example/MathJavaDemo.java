package com.example;

import java.io.PrintStream;

/**
 * Created by angcyo on 2016-09-26 16:47.
 */
public class MathJavaDemo {
    static final PrintStream p = System.out;

    public static void main(String... args) {
//        final double atan1 = Math.atan2(1, 1);
//        final double atan2 = Math.atan2(1.5, 1);
//        final double atan3 = Math.atan2(-0.5, 0);
//        p.println(atan2);
//        p.println(Math.toDegrees(atan1));
//        p.println(Math.toDegrees(atan2));
//        p.println(Math.toDegrees(atan3));

        for (int i = 0; i <= 60; i++) {
            p.println(i % 9);
        }

//        test1();
//        p.println(Math.ceil(3.1));
//        p.println(Math.floor(3.1));
    }

    private static void test1() {
        for (int i = 0; i < 360; i++) {
            p.print(Math.sin(Math.toRadians(i)));
            p.print(" ");
        }
        p.println();
        p.println("------------------------------------------------------------------------");
        for (int i = 0; i < 360; i++) {
            p.print(Math.cos(Math.toRadians(i)));
            p.print(" ");
        }
        p.println();
        p.println("------------------------------------------------------------------------");
        for (int i = -180; i < 180; i++) {
            p.print(Math.sin(Math.toRadians(i)));
            p.print(" ");
        }
    }
}
