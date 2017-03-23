package com.example;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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
//        test1();
        //test2();
        //test3();
        //test4();

        String signString = "angcyo/angcyo+angcyo=angcyo";
        p.println(signString);
        signString = signString.replaceAll("/", "_a").replaceAll("\\+", "_b").replaceAll("=", "_c");
        p.println(signString);
    }

    private static void test4() {
        String src = "720.0x1000.0";
        String[] xes = src.split("x");
        p(xes[0]);
    }

    private static void test3() {
        double num = 1_999.99999999;
        String result = "";
        result = BigDecimal.valueOf(num).setScale(2, BigDecimal.ROUND_DOWN).toString();
        if (result.length() >= 9) {
            p(result.substring(0, result.length() - 8) + "万");
        } else {
            p(result);
        }
    }

    private static void test2() {
        double num = 1230000000.99999999;
        String result = "";
        result = String.format("%.2f", num);
        p.println(result);

        if (num > 100_000) {
            //result = String.valueOf(num / 100_000);
            result = BigDecimal.valueOf(num).setScale(2, BigDecimal.ROUND_DOWN).toString();
            p.println(result);
        }

        result = BigDecimal.valueOf(2).setScale(2, BigDecimal.ROUND_DOWN).toString();//小数点后2位, 不四舍五入
        p(result);
        //BigDecimal.setScale();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        p(decimalFormat.format(num));
    }

    public static void p(Object object) {
        p.println(object);
    }

    private static void test1() {
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
