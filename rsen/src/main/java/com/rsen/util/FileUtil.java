package com.rsen.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DecimalFormat;

/**
 * 文件操作工具类
 * Created by angcyo on 15-12-16 016 15:14 下午.
 */
public class FileUtil {
    /**
     * 获取文件名(不包含扩展名)
     *
     * @param fileName the file name
     * @return the string
     */
    public static String getFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return fileName;
        }
        String[] ext = fileName.split("\\.");
        return ext[0];
    }


    /**
     * 删除文件夹
     *
     * @param path 文件夹路径
     */
    public static void delFolder(File path) {
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delFolder(files[i]);
                } else {
                    try {
                        files[i].delete();
                    } catch (Exception e) {
                    }
                }
            }
        }
        try {
            path.delete();
        } catch (Exception e) {
        }
    }

    /**
     * 复制文件夹
     *
     * @param from   the from
     * @param to     the to
     * @param delete 是否覆盖旧文件,false 会跳过复制
     * @param copy   复制的回调
     */
    public static void copyFolder(String from, String to, boolean delete, OnCopy copy) {
        File fromFile = new File(from);
        File toFile = new File(to);
        if (!fromFile.exists() || !fromFile.isDirectory()) {
            return;
        }
        try {
            toFile.mkdirs();
            for (File file : fromFile.listFiles()) {
                if (file.isFile()) {
                    copyFile(to + File.separator + file.getName(), file, delete);
                    copy.onCopy();
                }
                if (file.isDirectory()) {
                    copyFolder(file.getAbsolutePath(), toFile.getAbsolutePath() + File.separator + file.getName(),
                            delete, copy);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy file.
     *
     * @param targetFile the target file
     * @param file       the file
     */
    public static void copyFile(String targetFile, File file) {
        File fsd = new File(targetFile);
        if (fsd.exists()) {
            fsd.delete();
        }
        try {
            FileInputStream is = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[4096];
            int readLen = 0;
            while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                fos.write(buffer, 0, readLen);
            }
            fos.close();
            is.close();
        } catch (Exception e) {

        }
    }

    /**
     * Copy file.
     *
     * @param targetFile the target file
     * @param file       the file
     * @param delete     the delete
     */
    public static void copyFile(String targetFile, File file, boolean delete) {
        File fsd = new File(targetFile);
        if (fsd.exists()) {
            if (delete) {
                fsd.delete();
            } else {
                return;
            }
        }
        try {
            FileInputStream is = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[4096];
            int readLen = 0;
            while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                fos.write(buffer, 0, readLen);
            }
            fos.close();
            is.close();
        } catch (Exception e) {

        }
    }

    /**
     * 返回SD卡路径,如果没有返回默认的下载缓存路径
     *
     * @return the sD path
     */
    public static String getSDPath() {
        return isExternalStorageWritable() ? Environment
                .getExternalStorageDirectory().getPath() : Environment
                .getDownloadCacheDirectory().getPath();
    }

    /**
     * 判断是否有SD卡
     *
     * @return the boolean
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 格式化字节
     *
     * @param size the size
     * @return string
     * @date 2014年12月3日
     */
    public static String formatSize(long size) {
        if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("#.##");
            return "" + df.format(tmpSize) + "MB";
        } else if (size / 1024 > 0) {
            return "" + (size / (1024)) + "KB";
        } else
            return "" + size + "B";
    }

    /**
     * Format size string.
     *
     * @param size the size
     * @return the string
     */
    public static String formatSize(String size) {

        if (size.substring(size.length() - 1).equalsIgnoreCase("B")
                || size.endsWith("-")) {
            return size;
        } else {
            return formatSize(Long.valueOf(size));
        }

    }

    /**
     * 是否分割kb单位
     *
     * @param size      the size
     * @param isNewLine the is new line
     * @return string
     * @date 2014年12月3日
     */
    public static String formatSize(long size, boolean isNewLine) {
        if (isNewLine == false) {
            return formatSize(size);
        }
        if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("#.##");
            return "" + df.format(tmpSize) + "\nMB";
        } else if (size / 1024 > 0) {
            return "" + (size / (1024)) + "\nKB";
        } else
            return "" + size + "\nB";
    }

    public static void writeToFile(String data, String filePath, boolean isAppend) {
        try {
            FileWriter fileWriter = new FileWriter(new File(filePath), isAppend);
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 返回一个安全的文件路径
     */
    public static String getPath(String name) {
        File file = new File(getSDPath() + "/" + name);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        return file.getAbsolutePath();
    }

    /**
     * The type On copy.
     */
    public static abstract class OnCopy {
        /**
         * On copy.
         */
        public abstract void onCopy();
    }
}

