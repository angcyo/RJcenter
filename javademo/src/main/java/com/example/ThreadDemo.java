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

        ThreadNew threadNew = new ThreadNew();
        threadNew.start();
        threadNew.start();
    }


    static class ThreadNew extends Thread {
        @Override
        public void run() {
            super.run();
            p.println(Thread.currentThread().getId() + "-->" + System.currentTimeMillis());
        }
    }
}
