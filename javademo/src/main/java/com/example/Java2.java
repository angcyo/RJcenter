package com.example;

import com.util.Utility;

import java.io.PrintStream;

/**
 * Created by angcyo on 16-02-19-019.
 */
public class Java2 {
    static final PrintStream p = System.out;
    final static int max = 11;

    public static void main(String... args) {
//        test1();

        byte[] src = String.valueOf("angcyo").getBytes();

        byte[] b2 = String.valueOf(1).getBytes();

        byte[] dct = new byte[1024];

//        System.arraycopy(dct, 0, b2, 0, b2.length);
//        System.arraycopy(dct, 10, src, 0, src.length);

        byte[] head = new byte[10];
        System.arraycopy(b2, 0, head, 0, b2.length);

        System.arraycopy(head, 0, dct, 0, head.length);
        System.arraycopy(src, 0, dct, 10, src.length);

        byte[] dctHead = new byte[10];
        byte[] dctSrc = new byte[dct.length - dctHead.length];
        System.arraycopy(dct, 0, dctHead, 0, dctHead.length);
        System.arraycopy(dct, dctHead.length, dctSrc, 0, dctSrc.length);

        p.println(new String(dctHead));
        p.println(new String(dctSrc));

//        String s = new String(dct);
//        p.println(s);
//        p.println(new String(b2));
//        p.println(new String(src));
    }

    private static void test1() {
        for (int i = 0; i < max; i++) {
            p.println("\nint: " + i + " byte:");
            byte[] bytes = Utility.int2Byte(i);
            for (int j = 0; j < bytes.length; j++) {
                p.print(bytes[j]);
            }
        }

        for (int i = 0; i < max; i++) {
            p.println("\nint: " + i + " byte:");
            byte[] bytes = String.valueOf(i).getBytes();
            for (int j = 0; j < bytes.length; j++) {
                byte aByte = bytes[j];
                p.print(aByte);
                p.print("-");
                p.print(((int) aByte));
            }
        }
    }
}
