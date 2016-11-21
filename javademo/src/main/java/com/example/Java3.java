package com.example;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by robi on 2016-04-13 11:33.
 */
public class Java3 {
    public static PrintStream p = System.out;

    public static void main(String... args) {
//        test1();

//        test2();


//        test3();

//        test4();

        p.println("SV313".substring(2));
        p.println("SV313".substring(1,2));

        String str = new String("我是Robi");
        for (int i = 0; i < str.length(); i++) {
            p.print(str.charAt(i));
            p.print(" ");
        }
        p.print("\n");

        ArrayList list = new ArrayList();
        p.print(list.get(0));
    }

    private static void test4() {
        final String raw = "1234567891234567";
        StringBuilder result = new StringBuilder("**** **** **** ").append(raw.substring(raw.length() - 4));
        p.println(result);
    }

    private static void test3() {
        boolean b = false;

        b = b | false;
        p.println(b);

        b = b | true;
        p.println(b);

        b = b | false;
        p.println(b);

        b = b | true;
        p.println(b);
    }

    private static void test2() {
        int index = 0;
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
        p.println(index++ % 3);
    }

    private static void test1() {
        String string = "asdf";
        String[] split = string.split("|");
        p.println(split.length);
        int index = 1 % 0;

        p.println(index);

        index = split.length % 0;

        p.println(index);
    }
}
