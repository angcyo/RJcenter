package com.rsen.util;

import java.security.MessageDigest;

/**
 * Created by angcyo on 15-09-27-027.
 */
public class MD5 {

    /***
     * MD5加码 生成32位md5码
     */
    public static String toMD5(String source) {
        MessageDigest mDigest;
        StringBuffer hexString = new StringBuffer();
        try {
            mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(source.getBytes("UTF-8"));
            byte[] md5bytes = mDigest.digest();
            for (byte b : md5bytes) {
                if ((b & 0xFF) < 0x10)
                    hexString.append("0");
                hexString.append(Integer.toHexString(b & 0xFF));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    /***
     * MD5加码 生成48位md5码
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val) + 1);
        }
        return hexValue.toString();
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }
}
