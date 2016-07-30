package com.example;

import java.io.PrintStream;

/**
 * Created by angcyo on 2016-04-02 20:23.
 */
public class ThreadDemo {
    static final PrintStream p = System.out;

    public static void main(String... args) {
//        new Thread() {
//            @Override
//            public void run() {
//                long startTime = System.currentTimeMillis();
//                long index = 0;
//                while (true) {
//                    ++index;
//                    long nowTime = System.currentTimeMillis();
//                    if ((nowTime - startTime) / 1000 >= 1) {
//                        p.println(index);
//                        index = 0;
//                        startTime = nowTime;
////                        break;
//                    }
//                }
////                p.println(index);
//            }
//        }.start();

//        ThreadNew threadNew = new ThreadNew();
//        threadNew.start();
//        threadNew.start();

//        p.println(getNum());

        p.println(Math.ceil(1.1f));//2.0
        p.println(Math.round(1.1f));//1
        p.println(Math.ceil(-1.1f));//-1.0
        p.println(Math.round(-1.1f));//1
        p.println(Math.sqrt(Math.exp(3) + Math.exp(4)));
    }

    public static int getNum() {
        try {
            p.println("0--");
            int a = 1 / 0;
            p.println("1--");
            return 1;
        } catch (Exception e) {
            p.println("2--");
            return 2;
        } finally {
            int b = 1 / 0;
            p.println("3--");
            return 3;
        }

    }


    static class ThreadNew extends Thread {
        @Override
        public void run() {
            super.run();
            p.println(Thread.currentThread().getId() + "-->" + System.currentTimeMillis());
        }
    }
}
