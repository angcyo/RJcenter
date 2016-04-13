package com.example;

import java.io.PrintStream;

/**
 * Created by robi on 2016-04-13 11:33.
 */
public class Java3 {
    public static PrintStream p = System.out;

    public static void main(String... args) {
        String string = "asdf";
        String[] split = string.split("|");
        p.println(split.length);
        int index = 1 % 0;

        p.println(index);

        index = split.length % 0;

        p.println(index);
    }
}
