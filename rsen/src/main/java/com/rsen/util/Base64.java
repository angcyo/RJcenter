package com.rsen.util;

import java.io.UnsupportedEncodingException;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/11/22 14:32
 * 修改人员：Robi
 * 修改时间：2016/11/22 14:32
 * 修改备注：
 * Version: 1.0.0
 */
public class Base64 {

    /**
     * BASE64 加密
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 加密
            return new String(android.util.Base64.encode(encode, 0, encode.length, android.util.Base64.DEFAULT), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * BASE64 解密
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 解密
            return new String(android.util.Base64.decode(encode, 0, encode.length, android.util.Base64.DEFAULT), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }
}
