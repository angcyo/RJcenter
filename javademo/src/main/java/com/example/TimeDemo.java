package com.example;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        p.println(4 % 3);
        p.println(4 % 3 + 1);
        p.println(4 % (3 + 1));
    }

    public static String getSimpleDate(long time) {
        p.println("-" + time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }
}
