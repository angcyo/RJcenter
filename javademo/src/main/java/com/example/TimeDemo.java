package com.example;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/28 14:08
 * 修改人员：Robi
 * 修改时间：2016/10/28 14:08
 * 修改备注：
 * Version: 1.0.0
 */
public class TimeDemo {
    static final PrintStream p = System.out;

    public static void main(String... args) {
//        p.println(getSimpleDate(1_477_622_968));
//        p.println(getSimpleDate(1_477_622_968l * 1000));
//        p.println(1477622968 / 1000);
//        p.println(System.currentTimeMillis());
//        p.println(1477622968);

//        p.println(4 % 3);
//        p.println(4 % 3 + 1);
//        p.println(4 % (3 + 1));
//        p.println("--------------");
//        p.println(Math.ceil(3f / 4));
//        p.println(Math.ceil(4f / 4));
//        p.println(Math.ceil(5f / 4));
//        p.println(Math.ceil(4f / 4 + 1));
//        p.println(Math.ceil(4f / (4 + 1)));

        //main2("aaa", "bbb", "ccc");//test

        int[] origin = new int[]{1, 1, 2, 3, 1, 4, 8, 2, 7, 5, 4, 10, 6, 5, 9};
        List<Integer> originList = new ArrayList<>();

        List<Integer> resultList = new ArrayList<>();
        for (int n : origin) {
            if (originList.contains(n)) {
                if (resultList.contains(n)) {
                    resultList.add(n);
                } else {
                    resultList.add(n);
                    resultList.add(n);
                }
            } else {
                originList.add(n);
            }
        }
        for (Integer n : resultList) {
            p.print(" " + n);
        }
        p.println();
    }

    public static void main2(String... args) {
        p.println(args);
    }

    public static String getSimpleDate(long time) {
        p.println("-" + time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }
}
