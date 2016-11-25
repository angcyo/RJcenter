package com.example;

import java.io.PrintStream;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/28 11:49
 * 修改人员：Robi
 * 修改时间：2016/10/28 11:49
 * 修改备注：
 * Version: 1.0.0
 */
public class StringDemo {

    static final PrintStream p = System.out;

    public static String convert(String utfString) {
        StringBuilder sb = new StringBuilder();
        String lastString = "";
        int i;
        int pos = 0;
        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
            lastString = utfString.substring(pos);
        }
        sb.append(lastString);

        return sb.toString();
    }

    public static void main(String... args) {
        String string = "";
        String[] strings = string.split(",");

        string = ",";
        strings = string.split(",");

        string = "1,";
        strings = string.split(",");

        string = "1,2,3";
        strings = string.split(",");

        p.print("");
    }
}
