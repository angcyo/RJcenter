package com.example;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/28 11:50
 * 修改人员：Robi
 * 修改时间：2016/10/28 11:50
 * 修改备注：
 * Version: 1.0.0
 */
public class ReflectDemo {

    static final PrintStream p = System.out;

    public static void main(String... args) {
        TestClass testClass = new TestClass();

        for (Field f : testClass.getClass().getDeclaredFields()) {
            f.setAccessible(true);

            if (f.getType().getName().contains("List")) {
                p.println("is list");
            }

            try {
                p.println("name:" + f.getName() + " type:" + f.getType().getTypeName() + " " + f.getType().getSuperclass() + " value:" + f.get(testClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class TestClass {
        String str;
        int in;
        float flt;
        List<String> field1;
        ArrayList<TestClass> field2;
    }
}
