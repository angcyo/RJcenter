package com.example;

import java.io.PrintStream;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：进制表示方法
 * 创建人员：Robi
 * 创建时间：2017/04/17 10:17
 * 修改人员：Robi
 * 修改时间：2017/04/17 10:17
 * 修改备注：
 * Version: 1.0.0
 */
public class RadixDemo {
    static final PrintStream p = System.out;

    public static void main(String... args) {
        //十六进制
        int n = 0x10;
        p.println("" + n);//16
        //十进制
        n = 10;
        p.println("" + n);//10
        //八进制
        n = 010;
        p.println("" + n);//8
        //二进制
        n = 0b10;
        p.println("" + n);//2
    }
}
