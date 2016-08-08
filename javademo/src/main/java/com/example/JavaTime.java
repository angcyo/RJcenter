package com.example;

import java.io.PrintStream;
import java.util.Locale;

/**
 * Created by robi on 2016-08-08 13:20.
 */
public class JavaTime {
    static PrintStream p = System.out;

    public static void main(String... args) {
        final long timeMillis = System.currentTimeMillis();
        final long timeMillis2 = 1470133818996l;
        p.println(timeMillis);
        while (true) {
            p.println(test1(timeMillis2));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String test1(long connectTime) {
        long secs = (System.currentTimeMillis() - connectTime) / 1000;//总共多少秒
        int sec = (int) (secs % 60);//秒
        int mins = (int) (secs / 60);//总共多少分
        int min = mins;
        int hr = 0;
        if (mins > 59) {
            min = mins % 60;
            hr = mins / 60;
        }
        StringBuilder builder = new StringBuilder();
        if (hr > 0) {
            builder.append(String.format(Locale.CHINA, "%02d", hr)).append(":");
        }
        builder.append(String.format(Locale.CHINA, "%02d", min)).append(":");
        builder.append(String.format(Locale.CHINA, "%02d", sec));

        return builder.toString();
    }
}
