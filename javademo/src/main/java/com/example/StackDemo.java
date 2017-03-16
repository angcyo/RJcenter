package com.example;

import java.io.PrintStream;
import java.util.Stack;

/**
 * Created by angcyo on 2016-11-12.
 */

public class StackDemo {
    static final PrintStream p = System.out;

    public static void main(String... args) {
        Stack<String> stringStack = new Stack<>();


        p.println(stringStack.lastElement());
    }
}
