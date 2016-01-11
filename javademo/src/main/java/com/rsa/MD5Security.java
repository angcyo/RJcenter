package com.rsa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lhy
 * @description 详细描述：MD5加密，16位
 * @date 2014-4-2 上午9:53:53
 */
public class MD5Security {
    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }

    /**
     * 方法概述：MD516位加密
     *
     * @param @param  input
     * @param @param  bit
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @description 方法详细描述：
     * @author lhy
     * @Title: MD5Security.java
     * @Package com.huika.huixin.utils
     * @date 2014-4-2 上午11:17:18
     */
    public static String getMd5_16(String input, int bit) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
            if (bit == 16)
                return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
            return bytesToHex(md.digest(input.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception("Could not found MD5 algorithm.", e);
        }
    }

    /**
     * 方法概述：MD532位加密
     *
     * @param @param  plainText
     * @param @return
     * @return String
     * @throws
     * @description 方法详细描述：
     * @author lhy
     * @Title: MD5Security.java
     * @Package com.huika.huixin.utils
     * @date 2014-4-2 上午11:17:38
     */
    public static String getMd5_32(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    public static String getMd5_32_UP(String plainText) {
        String res = getMd5_32(plainText);
        return res.toUpperCase();
    }
}
