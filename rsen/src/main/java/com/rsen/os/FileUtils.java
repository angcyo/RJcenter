/*
 * Copyright (c) 2011,2013 Qualcomm Technologies inc, All Rights Reserved.
 * Qualcomm Technologies Confidential and Proprietary.
 */

package com.rsen.os;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * File copy/delete/move
 */
public class FileUtils {
    public static final String TAG = "FileExplorer";
    public static final String OPEN_PATH = "path";
    public static final String CATEGORY_FILE = "categoryFile";
    public static final String CATEGORY_TYPE = "categoryType";
    public static final String CATEGORY_PATH = "/category";
    public static final String REQUEST = "request";
    public static final String RESULT = "result";
    public static final String OPERATE_MODE = "operationMode";
    public static final String START_MODE = "startMode";
    public static final String USER_BACK = "userback";
    public static final int FILE_OPERATION_OK = 0;
    public static final int FILE_OP_ERR_MEMORY_FULL = 1;
    public static final int FILE_OP_ERR_NOT_SD_FILE = 2;
    public static final int FILE_OP_ERR_SRC_EQU_DEST = 3;
    public static final int FILE_OP_ERR_OTHER = 7;
    public static final int IMAGE_ICON_SIZE = 70;
    public static final long INVALID_NUM = -1;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_AUDIO = 1;
    private static final int TYPE_VIDEO = 2;
    private static final int TYPE_IMAGE = 3;
    public static long mCountSize = 0;
    public static long mCountNum = 0;
    public static int processEvent = 8;
    private static String GET_PHONE_STORAGE_DIR_METHOD_NAME = "getExternalStorageDirectory";
    private static String GET_PHONE_STORAGE_STATE_METHOD_NAME = "getExternalStorageState";

    /**
     * Delete files or folders
     */
    public static boolean deleteFiles(Context context, final String path, Handler handler,
                                      long mTotalFileNum) {

//        if (!isFileOnExternalDrive(context, path)) {
//            return false;
//        }

        boolean ret = true;
        File f = new File(path);

        if (!f.isDirectory()) {
            ret = f.delete();
            sendProgressMessage(handler, mTotalFileNum);

            return ret;
        }

        Stack<File> stack = new Stack<File>();
        Stack<File> stackDir = new Stack<File>();

        stack.push(f);
        stackDir.push(f);

        // remove file here
        while (!stack.isEmpty() && (null != (f = stack.pop()))) {

            for (File file : f.listFiles()) {

                if (file.isDirectory()) {
                    stack.push(file);
                    stackDir.push(file);
                } else {
                    ret = file.delete();
                    sendProgressMessage(handler, mTotalFileNum);
                }

                if (ret == false) return false;
            }
        }

        // remove the empty directory
        while (!stackDir.isEmpty() && (null != (f = stackDir.pop()))) {
            ret = f.delete();

            if (ret == false) return false;
        }

        return ret;
    }

    private static void sendProgressMessage(Handler handler, long mTotalFileNum) {
        if (mTotalFileNum == INVALID_NUM) {
            return;

        } else {
            mCountNum++;
            int number = (int) ((100 * mCountNum) / mTotalFileNum);

            Message progressMessage = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("event", processEvent);
            b.putInt("number", number);
            progressMessage.setData(b);
            handler.sendMessage(progressMessage);

            // When we delete all files,reset the counter to zero
            if (mCountNum >= mTotalFileNum) {
                mCountNum = 0;
            }
        }
    }

    /**
     * Copy folders
     */
    private static boolean copyDir(Context context, File srcDir, File dstDir, Handler handler,
                                   long totalSize) {

        if (!ableToCreateDir(dstDir)) {
            return false;
        }

        Queue<File> queue = new LinkedList<File>();
        queue.offer(srcDir);

        Queue<File> queueDst = new LinkedList<File>();
        queueDst.offer(dstDir);

        File files = null;

        while (null != (files = queue.poll()) && null != (dstDir = queueDst.poll())) {

            for (File file : files.listFiles()) {

                File dstFile = new File(dstDir, file.getName());

                if (file.isDirectory()) {
                    if (!ableToCreateDir(dstFile)) return false;

                    queue.offer(file);
                    queueDst.offer(dstFile);
                } else {
                    copyFile(context, file, dstFile, handler, totalSize);
                }
            }
        }

        return true;
    }

    /**
     * Copy binary file
     */
    public static boolean copyFile(Context context, File srcFile, File dstFile, Handler handler,
                                   long totalSize) {
        try {
//            if (!isFileOnExternalDrive(context, srcFile.getPath())) {
//                return false;
//            }

            InputStream in = new FileInputStream(srcFile);
            if (dstFile.exists()) {
                dstFile.delete();
            }

            OutputStream out = new FileOutputStream(dstFile);
            try {
                int cnt;
                byte[] buf = new byte[4096];
                while ((cnt = in.read(buf)) >= 0) {
                    out.write(buf, 0, cnt);
                    mCountSize = mCountSize + cnt;
                    int number = (int) ((100 * mCountSize) / totalSize);
                    Message progressMessage = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("event", processEvent);
                    b.putInt("number", number);
                    progressMessage.setData(b);
                    handler.sendMessage(progressMessage);
                }
                if (mCountSize >= totalSize) {
                    mCountSize = 0;
                }
            } finally {
                out.close();
                in.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean ableToCreateDir(File dstDir) {

        if (dstDir.exists()) {
            if (dstDir.isDirectory() == false) {
                return false;
            }
        } else {
            if (dstDir.mkdirs() == false) {
                return false;
            }
        }

        if (dstDir.canWrite() == false) {
            return false;
        }

        return true;
    }

    private static boolean isFileOnExtSdcard(final String srcFilePath, final String extSdcardPath) {
        return (isPathValid(extSdcardPath) && (srcFilePath.startsWith(extSdcardPath, 0)));
    }

    private static boolean isFileOnIntSdcard(final String srcFilePath, final String intSdcardPath) {
        return (isPathValid(intSdcardPath) && (srcFilePath.startsWith(intSdcardPath, 0)));
    }

    private static boolean isFileOnUsbStorage(final String srcFilePath, final String extUsbPath) {
        return (isPathValid(extUsbPath) && (srcFilePath.startsWith(extUsbPath, 0)));
    }

    private static boolean isFileOnUiccStorage(final String srcFilePath, final String extUiccPath) {
        return (isPathValid(extUiccPath) && (srcFilePath.startsWith(extUiccPath, 0)));
    }

    public static boolean fileExist(String fileName) {
        boolean ret = false;

        File f = new File(fileName);
        if (f.exists()) {
            ret = true;
        }

        return ret;
    }

    public static String getInternalPath() {
        File sd = null;
        sd = Environment.getExternalStorageDirectory();
        if (sd != null) {
            return sd.toString();
        } else
            return null;
    }

    public static String getFavoritePath() {
        return getInternalPath() + "/DCIM/Camera";
    }

    private static boolean isPathValid(String filePath) {
        return ((filePath != null) && (filePath.length() > 0));
    }

    // add check free space function
    // true is over free space
    public static boolean checkRemainSpace(File srcFile, File dstFile) {
        if ((getAvailableAnySpace(dstFile.getParent()) - srcFile.length()) >= 0)
            return false;

        return true;
    }

    /**
     * Get the file type
     *
     * @param file the file
     */
    public static int getFileType(File file) {
        String path = file.getPath();
        String mimeType = MediaFile.getMimeTypeForFile(path);
        return MediaFile.getFileTypeForMimeType(mimeType);
    }

    /**
     * Decode and covert a image to a small square icon bitmap for display.
     *
     * @param file   the image file
     * @param bitmap the icon bitmap
     */
    public static Bitmap getImageThumbnail(File file, Bitmap bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // Decode the width and height of the bitmap, but don't load the bitmap
        // to RAM
        BitmapFactory.decodeFile(file.getPath(), options);

        int max = Math.max(options.outHeight, options.outWidth);

        // Compute the sampleSize of the options
        int size = (int) (max / (float) Math.max(bitmap.getWidth(), bitmap.getHeight()));
        if (size <= 0) {
            size = 1;
        }
        options.inSampleSize = size;

        // Decode the width and height of the bitmap and load the bitmap to RAM
        options.inJustDecodeBounds = false;

        Bitmap iconBitmap = BitmapFactory.decodeFile(file.getPath(), options);

        iconBitmap = ThumbnailUtils.extractThumbnail(iconBitmap, IMAGE_ICON_SIZE, IMAGE_ICON_SIZE,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return iconBitmap;
    }

    /**
     * Compute the file length.
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }

        if (!file.isDirectory()) {
            return file.length();
        }

        long size = 0;

        Queue<File> queue = new LinkedList<File>();
        queue.offer(file);

        while (null != (file = queue.poll())) {

            for (File fileTemp : file.listFiles()) {
                if (!fileTemp.isDirectory()) {
                    size += fileTemp.length();
                } else {
                    queue.offer(fileTemp);
                }
            }
        }

        return size;
    }

    // Get available space of local file folder.
    public static long getAvailableAnySpace(String path) {
        long remaining = 0;
        try {
            StatFs stat = new StatFs(path);
            remaining = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            // do nothing
        }
        return remaining;
    }

    public static boolean internalStorageExist() {
        boolean ret = false;

        if (null != getInternalStoragePath()
                && Environment.MEDIA_MOUNTED.equals(getInternalStorageState())) {
            ret = true;
        }

        return ret;
    }

    public static String getInternalStorageState() {

        String ret = null;
        try {
            Method method = Environment.class.getMethod(GET_PHONE_STORAGE_STATE_METHOD_NAME,
                    (Class[]) null);
            ret = (String) method.invoke(null, (Object[]) null);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return ret;
    }

    public static String getInternalStoragePath() {
        String ret = null;

        try {
            Method method = Environment.class.getMethod(GET_PHONE_STORAGE_DIR_METHOD_NAME,
                    (Class[]) null);
            File file = (File) method.invoke(null, (Object[]) null);
            ret = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void openFile(Context context, File f) {
        // We will first guess the MIME type of this file
        final Uri fileUri = Uri.fromFile(f);
        final Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        // Send file name to system application for sometimes it will be used.
        intent.putExtra(Intent.EXTRA_TITLE, f.getName());
        intent.putExtra("org.codeaurora.intent.extra.ALL_VIDEO_FOLDER", true);
        Uri contentUri = null;

        String type = getMIMEType(f);
        // For image file, content uri was needed in Gallery,
        // in order to know the pre/next image.
        if (type != null && type.contains("image/")) {
            String path = fileUri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Images.ImageColumns.DATA)
                        .append("=")
                        .append("'" + path + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                if (cur != null) {
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                        index = cur.getInt(index);
                    }
                }
                if (index == 0) {
                    //not found, do nothing
                    Log.d(TAG, "file not found");
                } else {
                    contentUri = Uri.parse("content://media/external/images/media/" + index);
                }

            }
        }
        if (!"application/octet-stream".equals(type)) {
            if (contentUri != null) {
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setDataAndType(fileUri, type);
            }
            // If no activity can handle the intent then
            // give user a chooser dialog.
            try {
                startActivitySafely(context, intent);

            } catch (ActivityNotFoundException e) {
                showChooserDialog(context, fileUri, intent);
            }
        } else {
            showChooserDialog(context, fileUri, intent);
        }
    }

    private static void showChooserDialog(Context context, final Uri fileUri, final Intent intent) {
        // If this file type can not be recognized then give user a chooser
        // dialog, providing 4 alternative options to open this file: open
        // as plain text, audio, video or image. And the corresponding MIME
        // type will be set to "text/plain", "audio/*", "video/*" and
        // "image/*". By this we can avoid many alternative activity entries
        // from one package in the action chooser dialog.
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("打开为")
                .setItems(
                        new String[]{
                                "文本",
                                "音频",
                                "视频",
                                "图片"
                        },
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case TYPE_TEXT:
                                        intent.setDataAndType(fileUri,
                                                "text/plain");
                                        break;

                                    case TYPE_AUDIO:
                                        intent.setDataAndType(fileUri,
                                                "audio/*");
                                        break;

                                    case TYPE_VIDEO:
                                        intent.setDataAndType(fileUri,
                                                "video/*");
                                        break;

                                    case TYPE_IMAGE:
                                        intent.setDataAndType(fileUri,
                                                "image/*");
                                        break;

                                    default:
                                        break;
                                }
                                startActivitySafely(context, intent);
                            }
                        }).create();
        alertDialog.show();
    }

    public static void startActivitySafely(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context,
                    "Activity Not Found.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public static String getMIMEType(File f) {
        String type = "";
        String fileName = f.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".")
                + 1, fileName.length()).toLowerCase();

        if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif")
                || ext.equals("png") || ext.equals("bmp")) {
            type = "image/*";
        } else if (ext.equals("mp3") || ext.equals("amr") || ext.equals("wma")
                || ext.equals("aac") || ext.equals("m4a") || ext.equals("mid")
                || ext.equals("xmf") || ext.equals("ogg") || ext.equals("wav")
                || ext.equals("qcp") || ext.equals("awb") || ext.equals("flac")) {
            type = "audio/*";
        } else if (ext.equals("3gp") || ext.equals("avi") || ext.equals("mp4")
                || ext.equals("3g2") || ext.equals("wmv") || ext.equals("divx")
                || ext.equals("mkv") || ext.equals("webm") || ext.equals("ts")
                || ext.equals("asf") || ext.equals("3gpp")) {
            type = "video/*";
        } else if (ext.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else if (ext.equals("vcf")) {
            type = "text/x-vcard";
        } else if (ext.equals("txt")) {
            type = "text/plain";
        } else if (ext.equals("doc") || ext.equals("docx")) {
            type = "application/msword";
        } else if (ext.equals("xls") || ext.equals("xlsx")) {
            type = "application/vnd.ms-excel";
        } else if (ext.equals("ppt") || ext.equals("pptx")) {
            type = "application/vnd.ms-powerpoint";
        } else if (ext.equals("pdf")) {
            type = "application/pdf";
        } else if (ext.equals("xml")) {
            type = "text/xml";
        } else if (ext.equals("html")) {
            type = "text/html";
        } else if (ext.equals("zip")) {
            type = "application/zip";
        } else {
            type = "application/octet-stream";
        }

        return type;
    }
}
