package com.rsen.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * 配置属性读写工具类
 * Created by angcyo on 15-12-16 016 15:15 下午.
 */
public class Config {

    private static String CONFIG_PATH = Environment.getExternalStorageDirectory() + "/rsen_ini.ini";

    static {
        init(CONFIG_PATH);
    }

    /**
     * 设置配置文件的完整路径,包括文件名
     *
     * @param newPath the new path
     */
    public static void setConfigPath(String newPath) {
        init(newPath);
        File file = new File(newPath);
        if (file.exists()) {
            CONFIG_PATH = newPath;
        }
    }

    private static void init(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取一个key值
     *
     * @param key the key
     * @return the string
     */
    public static String get(String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }

        Reader in = null;
        String value = "";
        try {
            Properties properties = new Properties();
            in = new InputStreamReader(new FileInputStream(new File(CONFIG_PATH)));
            properties.load(in);

            value = properties.getProperty(key, "");
        } catch (IOException e) {
            value = "";
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }

        return value;
    }

    /**
     * Get string [ ].
     *
     * @param keys the keys
     * @return the string [ ]
     */
    public static String[] get(String... keys) {
        if (keys == null) {
            return new String[]{};
        }
        int length = keys.length;
        String[] values = new String[length];

        for (int i = 0; i < length; i++) {
            values[i] = get(keys[i]);
        }
        return values;
    }

    /**
     * 设置值.
     *
     * @param key   the key
     * @param value the value
     */
    public static void set(String key, String value) {
        Reader in = null;
        Writer out = null;
        try {
            Properties properties = new Properties();
            in = new InputStreamReader(new FileInputStream(new File(CONFIG_PATH)));
            out = new OutputStreamWriter(new FileOutputStream(new File(CONFIG_PATH)));
            properties.load(in);
            properties.setProperty(key, value);
            properties.store(out, "power by angcyo");
        } catch (IOException e) {
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Set.
     *
     * @param keys   the keys
     * @param values the values
     */
    public static void set(String[] keys, String... values) {
        for (int i = 0; i < keys.length; i++) {
            set(keys[i], values[i]);
        }
    }
}
