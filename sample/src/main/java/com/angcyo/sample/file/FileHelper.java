package com.angcyo.sample.file;

import java.io.File;
import java.io.IOException;

/**
 * Created by robi on 2016-07-15 20:26.
 */
public class FileHelper {
    public static void createFile(String file) {
        File newFile = new File(file);
        if (newFile.exists()) {
            newFile.delete();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setOnlyRead(String file) {
        File newFile = new File(file);
        if (newFile.exists()) {
            newFile.setReadable(true);
        }
    }

    public static void setOnlyWrite(String file) {
        File newFile = new File(file);
        if (newFile.exists()) {
            newFile.setWritable(true);
        }
    }

    public static boolean canExecute(String file) {
        File newFile = new File(file);
        return newFile.canExecute();
    }

    public static boolean canRead(String file) {
        File newFile = new File(file);
        return newFile.canRead();
    }

    public static boolean canWrite(String file) {
        File newFile = new File(file);
        return newFile.canWrite();
    }
}
