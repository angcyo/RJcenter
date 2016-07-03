package com.rsen.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by angcyo on 15-12-13 011 09:58 上午.
 */
public final class LLog {

    /**
     * 表示关闭LOG输出 *
     */
    public static final int LEVEL_OFF = Integer.MAX_VALUE;

    /**
     * The constant TAG_DEBUG.
     */
    public static final String TAG_DEBUG = "RsenL";
    private static final String FILE_LOG_DIR = "logs";

    private static Map<String, Long> sTraceMap = new HashMap<String, Long>();
    private static FileLogger sFileLogger;
    // 默认情况下log输出ERROR级别，file log不输出
    private static int sLoggingLevel = Log.ERROR;
    private static int sFileLoggingLevel = Log.ASSERT;

    private static boolean needLog(int level) {
        return level >= sLoggingLevel;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public static void setLevel(int level) {
        sLoggingLevel = level;
    }

    /**
     * Sets file logging level.
     *
     * @param appContext the app context
     * @param level      the level
     */
    public static void setFileLoggingLevel(Context appContext, int level) {
        sFileLoggingLevel = level;
        openFileLogger(appContext);
    }

    /**
     * E.
     *
     * @param t the t
     */
    public static void e(Throwable t) {
        e(TAG_DEBUG, t);
    }

    /**
     * E.
     *
     * @param tag the tag
     * @param e   the e
     */
    public static void e(String tag, Throwable e) {
        if (needLog(Log.ERROR)) {
            Log.e(tag, "", e);

        }
    }

    /**
     * E.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void e(String tag, String message) {
        if (needLog(Log.ERROR)) {
            Log.e(tag, message);
        }
    }

    /**
     * W.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void w(String tag, String message) {
        if (needLog(Log.WARN)) {
            Log.w(tag, message);
        }
    }

    /**
     * .
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void i(String tag, String message) {
        if (needLog(Log.INFO)) {
            Log.i(tag, message);
        }
    }

    /**
     * D.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void d(String tag, String message) {
        if (needLog(Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    /**
     * V.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void v(String tag, String message) {
        if (needLog(Log.VERBOSE)) {
            Log.v(tag, message);
        }
    }

    /**
     * V.
     *
     * @param format the format
     * @param args   the args
     */
    public static void v(String format, Object... args) {
        if (needLog(Log.VERBOSE)) {
            Log.v(TAG_DEBUG, buildMessage(format, args));
        }
    }

    /**
     * D.
     *
     * @param format the format
     * @param args   the args
     */
    public static void d(String format, Object... args) {
        if (needLog(Log.DEBUG)) {
            Log.d(TAG_DEBUG, buildMessage(format, args));
        }
    }

    /**
     * .
     *
     * @param format the format
     * @param args   the args
     */
    public static void i(String format, Object... args) {
        if (needLog(Log.INFO)) {
            Log.i(TAG_DEBUG, buildMessage(format, args));
        }
    }

    /**
     * W.
     *
     * @param format the format
     * @param args   the args
     */
    public static void w(String format, Object... args) {
        if (needLog(Log.WARN)) {
            Log.w(TAG_DEBUG, buildMessage(format, args));
        }
    }

    /**
     * E.
     *
     * @param format the format
     * @param args   the args
     */
    public static void e(String format, Object... args) {
        if (needLog(Log.ERROR)) {
            Log.e(TAG_DEBUG, buildMessage(format, args));
        }
    }

    /**
     * E.
     *
     * @param tr     the tr
     * @param format the format
     * @param args   the args
     */
    public static void e(Throwable tr, String format, Object... args) {
        if (needLog(Log.ERROR)) {
            Log.e(TAG_DEBUG, buildMessage(format, args), tr);
        }
    }

    /**
     * E.
     *
     * @param clz     the clz
     * @param message the message
     */
    public static void e(Class<?> clz, String message) {
        e(clz.getSimpleName(), message);
    }

    /**
     * W.
     *
     * @param clz     the clz
     * @param message the message
     */
    public static void w(Class<?> clz, String message) {
        w(clz.getSimpleName(), message);
    }

    /**
     * .
     *
     * @param clz     the clz
     * @param message the message
     */
    public static void i(Class<?> clz, String message) {
        i(clz.getSimpleName(), message);
    }

    /**
     * D.
     *
     * @param clz     the clz
     * @param message the message
     */
    public static void d(Class<?> clz, String message) {
        d(clz.getSimpleName(), message);
    }

    /**
     * V.
     *
     * @param clz     the clz
     * @param message the message
     */
    public static void v(Class<?> clz, String message) {
        v(clz.getSimpleName(), message);
    }

    /**
     * E.
     *
     * @param clz the clz
     * @param t   the t
     */
    public static void e(Class<?> clz, Throwable t) {
        e(clz.getSimpleName(), t);
    }

    /**
     * E.
     *
     * @param message the message
     */
    public static void e(String message) {
        if (needLog(Log.ERROR)) {
            Log.e(TAG_DEBUG, getMethodInfo(4));
            Log.e(TAG_DEBUG, "Message:\t" + message);
        }
    }

    /**
     * W.
     *
     * @param message the message
     */
    public static void w(String message) {
        if (needLog(Log.WARN)) {
            Log.w(TAG_DEBUG, getMethodInfo(4));
            Log.w(TAG_DEBUG, "Message:\t" + message);
        }
    }

    /**
     * .
     *
     * @param message the message
     */
    public static void i(String message) {
        if (needLog(Log.INFO)) {
            Log.i(TAG_DEBUG, getMethodInfo(4));
            Log.i(TAG_DEBUG, "Message:\t" + message);
        }
    }

    /**
     * D.
     *
     * @param message the message
     */
    public static void d(String message) {
        if (needLog(Log.DEBUG)) {
            Log.d(TAG_DEBUG, getMethodInfo(4));
            Log.d(TAG_DEBUG, "Message:\t" + message);
        }
    }

    /**
     * V.
     *
     * @param message the message
     */
    public static void v(String message) {
        if (needLog(Log.VERBOSE)) {
            Log.v(TAG_DEBUG, getMethodInfo(4));
            Log.v(TAG_DEBUG, "Message:\t" + message);
        }
    }

    private static String getMethodInfo(int index) {
        final Thread current = Thread.currentThread();
        final StackTraceElement[] stack = current.getStackTrace();
        final StackTraceElement element = stack[index];
        if (!element.isNativeMethod()) {
            final String className = element.getClassName();
            final String fileName = element.getFileName();
            final int lineNumber = element.getLineNumber();
            final String methodName = element.getMethodName();
            return "Method:\t" + className + "." + methodName + "() (" + fileName + ":" + lineNumber + ")";
        }
        return "";
    }

    /**
     * 获取StackTrace信息
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
// Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }

    /**
     * Start trace.
     *
     * @param operation the operation
     */
    public static void startTrace(String operation) {
        sTraceMap.put(operation, System.currentTimeMillis());
    }

    /**
     * Stop trace.
     *
     * @param operation the operation
     */
    public static void stopTrace(String operation) {
        Long start = sTraceMap.remove(operation);
        if (start != null) {
            long end = System.currentTimeMillis();
            long interval = end - start;
            Log.v(TAG_DEBUG, operation + " use time: " + interval + "ms");
        }
    }

    /**
     * Remove trace.
     *
     * @param key the key
     */
    public static void removeTrace(String key) {
        sTraceMap.remove(key);
    }

    /**
     * Clear trace.
     */
    public static void clearTrace() {
        sTraceMap.clear();
        Log.v(TAG_DEBUG, "trace is cleared.");
    }


    /**
     * 写log到文件
     *
     * @param tag the tag
     * @param e   the e
     */
    public static void fe(String tag, Throwable e) {
        fe(tag, "", e);
    }

    /**
     * Fe.
     *
     * @param tag     the tag
     * @param message the message
     * @param e       the e
     */
    public static void fe(String tag, String message, Throwable e) {
        if (needLog(Log.ERROR)) {
            Log.e(tag, "", e);

        }
        if (isFileLoggable(Log.ERROR)) {
            if (sFileLogger != null) {
                sFileLogger.e(tag, message, e);
            }
        }
    }

    /**
     * Fe.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void fe(String tag, String message) {
        if (needLog(Log.ERROR)) {
            Log.e(tag, message);
        }
        if (isFileLoggable(Log.ERROR)) {
            if (sFileLogger != null) {
                sFileLogger.e(tag, message);
            }
        }
    }

    /**
     * Fw.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void fw(String tag, String message) {
        if (needLog(Log.WARN)) {
            Log.w(tag, message);
        }
        if (isFileLoggable(Log.WARN)) {
            if (sFileLogger != null) {
                sFileLogger.w(tag, message);
            }
        }
    }

    /**
     * Fi.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void fi(String tag, String message) {
        if (needLog(Log.INFO)) {
            Log.i(tag, message);
        }
        if (isFileLoggable(Log.INFO)) {
            if (sFileLogger != null) {
                sFileLogger.i(tag, message);
            }
        }
    }

    /**
     * Fd.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void fd(String tag, String message) {
        if (needLog(Log.DEBUG)) {
            Log.d(tag, message);
        }
        if (isFileLoggable(Log.DEBUG)) {
            if (sFileLogger != null) {
                sFileLogger.d(tag, message);
            }
        }
    }

    /**
     * Fv.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void fv(String tag, String message) {
        if (needLog(Log.VERBOSE)) {
            Log.v(tag, message);
        }
        if (isFileLoggable(Log.VERBOSE)) {
            if (sFileLogger != null) {
                sFileLogger.v(tag, message);
            }
        }
    }


    /**
     * *****
     * File Logger相关
     */

    private static boolean isFileLoggable(int level) {
        return level >= sFileLoggingLevel;
    }

    private static void closeFileLogger() {
        if (sFileLogger != null) {
            sFileLogger.close();
            sFileLogger = null;
        }
    }

    private static void openFileLogger(Context context) {
        closeFileLogger();
        if (sFileLoggingLevel < Log.ASSERT) {
            sFileLogger = new FileLogger(TAG_DEBUG, createFileLogDirIfNeeded(context));
        }
    }

    private static File createFileLogDirIfNeeded(Context context) {
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = new File(context.getExternalCacheDir(), FILE_LOG_DIR);
        } else {
            dir = new File(context.getCacheDir(), FILE_LOG_DIR);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    /**
     * Clear log files.
     *
     * @param context the context
     */
    public void clearLogFiles(Context context) {
        File logDir = createFileLogDirIfNeeded(context);
        logDir.delete();
    }

    /**
     * Clear log files async.
     *
     * @param context the context
     */
    public void clearLogFilesAsync(final Context context) {
        new Thread() {
            @Override
            public void run() {
                clearLogFiles(context);
            }
        }.start();
    }

    private static class LogEntry {
        private static SimpleDateFormat dateFormat; // must always be used in the same thread
        private static Date mDate;

        private final long now;
        private final char level;
        private final String tag;
        private final String threadName;
        private final String msg;
        private final Throwable cause;
        private String date;

        /**
         * Instantiates a new Log entry.
         *
         * @param lvl        the lvl
         * @param tag        the tag
         * @param threadName the thread name
         * @param msg        the msg
         * @param tr         the tr
         */
        LogEntry(char lvl, String tag, String threadName, String msg, Throwable tr) {
            this.now = System.currentTimeMillis();
            this.level = lvl;
            this.tag = tag;
            this.threadName = threadName;
            this.msg = msg;
            this.cause = tr;
        }

        private void addCsvHeader(final StringBuilder csv) {
            if (dateFormat == null)
                dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
            if (date == null) {
                if (null == mDate)
                    mDate = new Date();
                mDate.setTime(now);
                date = dateFormat.format(mDate);
            }

            csv.append(date);
            csv.append(',');
            csv.append(level);
            csv.append(',');
            csv.append(android.os.Process.myPid());
            csv.append(',');
            if (threadName != null)
                csv.append(threadName);
            csv.append(',');
            csv.append(',');
            if (tag != null)
                csv.append(tag);
            csv.append(',');
        }

        private void addException(final StringBuilder csv, Throwable tr) {
            if (tr == null)
                return;
            final StringBuilder sb = new StringBuilder(256);
            sb.append(cause.getClass());
            sb.append(": ");
            sb.append(cause.getMessage());
            sb.append('\n');

            for (StackTraceElement trace : cause.getStackTrace()) {
//addCsvHeader(csv);
                sb.append(" at ");
                sb.append(trace.getClassName());
                sb.append('.');
                sb.append(trace.getMethodName());
                sb.append('(');
                sb.append(trace.getFileName());
                sb.append(':');
                sb.append(trace.getLineNumber());
                sb.append(')');
                sb.append('\n');
            }

            addException(sb, tr.getCause());
            csv.append(sb.toString().replace(';', '-').replace(',', '-').replace('"', '\''));
        }

        /**
         * Format csv char sequence.
         *
         * @return the char sequence
         */
        public CharSequence formatCsv() {
            final StringBuilder csv = new StringBuilder(256);
            addCsvHeader(csv);
            csv.append('"');
            if (msg != null) csv.append(msg.replace(';', '-').replace(',', '-').replace('"', '\''));
            csv.append('"');
            csv.append('\n');
            if (cause != null) {
                addCsvHeader(csv);
                csv.append('"');
                addException(csv, cause);
                csv.append('"');
                csv.append('\n');
            }
            return csv.toString();
        }
    }

    private static class FileLogger implements Handler.Callback {
        /**
         * The constant TAG.
         */
        public static final String TAG = FileLogger.class.getSimpleName();
        private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HH", Locale.US);
        private static final String UTF_8 = "UTF-8";
        private static final int MSG_OPEN = 0;
        private static final int MSG_WRITE = 1;
        private static final int MSG_CLEAR = 2;
        /**
         * The constant MAX_FILE_SIZE.
         */
        public static long MAX_FILE_SIZE = 1024 * 1024 * 10;
        private File mLogDir;
        private File mLogFile;
        private String mTag;
        private HandlerThread mHandlerThread;
        private Handler mAsyncHandler;
        private Writer mWriter;

        /**
         * Instantiates a new File logger.
         *
         * @param logTag the log tag
         * @param logDir the log dir
         */
        public FileLogger(String logTag, File logDir) {
            mTag = logTag;
            mLogDir = logDir;
            mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();
            mAsyncHandler = new Handler(mHandlerThread.getLooper(), this);
            sendOpenMessage();
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN:
                    onMessageOpen(msg);
                    break;
                case MSG_WRITE:
                    onMessageWrite(msg);
                    break;
                case MSG_CLEAR:
                    onMessageClear();
                    break;
            }
            return true;
        }

        /**
         * Close.
         */
        public void close() {
            Handler handler = mAsyncHandler;
            mAsyncHandler = null;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            if (mHandlerThread != null) {
                mHandlerThread.quit();
                mHandlerThread = null;
            }
        }

        /**
         * Clear.
         */
        public void clear() {
            sendClearMessage();
        }

        private void onMessageOpen(Message msg) {
            closeWriter();
            openWriter();
        }

        private void onMessageWrite(Message msg) {
            try {
                LogEntry logMessage = (LogEntry) msg.obj;
                if (mWriter != null) {
                    mWriter.append(logMessage.formatCsv());
                    mWriter.flush();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getClass().getSimpleName() + " : " + e.getMessage());
            }

            verifyFileSize();
        }

        private void onMessageClear() {
            if (mLogFile != null) {
                closeWriter();
                mLogDir.delete();
                openWriter();
            }
        }

        private void sendOpenMessage() {
            if (mAsyncHandler != null) {
                mAsyncHandler.sendEmptyMessage(MSG_OPEN);
            }
        }

        private void sendWriteMessage(LogEntry log) {
            if (mAsyncHandler != null) {
                Message message = mAsyncHandler.obtainMessage(MSG_WRITE, log);
                mAsyncHandler.sendMessage(message);
            }
        }

        private void sendClearMessage() {
            if (mAsyncHandler != null) {
                mAsyncHandler.sendEmptyMessage(MSG_CLEAR);
            }
        }

        private void createLogFile() {
            if (mLogDir == null) {
                return;
            }
            if (!mLogDir.exists()) {
                mLogDir.mkdirs();
            }
            String fileName = String.format("log-%1$s.%2$s", DATE_FORMAT.format(new Date()), "txt");
            File file = new File(mLogDir, fileName);
            try {
                file.createNewFile();
                mLogFile = file;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "createLogFile ex=" + e);
            }
        }

        private void openWriter() {
            createLogFile();
            if (mLogFile == null) {
                return;
            }
            if (mWriter == null)
                try {
                    mWriter = new OutputStreamWriter(new FileOutputStream(mLogFile, true), UTF_8);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "can't get a writer for " + mLogFile + " : " + e.getMessage());
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "can't get a writer for " + mLogFile + " : " + e.getMessage());
                }
        }

        private void closeWriter() {
            if (mWriter != null) {
                try {
                    mWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mWriter = null;
            }
        }

        private void verifyFileSize() {
            if (mLogFile != null) {
                long size = mLogFile.length();
                if (size > MAX_FILE_SIZE) {
                    closeWriter();
                    mLogFile.delete();
                    openWriter();
                }
            }
        }

        /**
         * D.
         *
         * @param tag the tag
         * @param msg the msg
         */
        public void d(String tag, String msg) {
            write('d', tag, msg);
        }

        /**
         * D.
         *
         * @param msg the msg
         */
        public void d(String msg) {
            write('d', msg);
        }

        /**
         * E.
         *
         * @param tag the tag
         * @param msg the msg
         * @param tr  the tr
         */
        public void e(String tag, String msg, Throwable tr) {
            write('e', tag, msg, tr);
        }

        /**
         * E.
         *
         * @param tag the tag
         * @param msg the msg
         */
        public void e(String tag, String msg) {
            write('e', tag, msg);
        }

        /**
         * E.
         *
         * @param msg the msg
         */
        public void e(String msg) {
            write('e', msg);
        }

        /**
         * .
         *
         * @param msg the msg
         * @param tag the tag
         */
        public void i(String msg, String tag) {
            write('i', tag, msg);
        }

        /**
         * .
         *
         * @param msg the msg
         */
        public void i(String msg) {
            write('i', msg);
        }

        /**
         * V.
         *
         * @param msg the msg
         * @param tag the tag
         */
        public void v(String msg, String tag) {
            write('v', tag, msg);
        }

        /**
         * V.
         *
         * @param msg the msg
         */
        public void v(String msg) {
            write('v', msg);
        }

        /**
         * W.
         *
         * @param tag the tag
         * @param msg the msg
         * @param tr  the tr
         */
        public void w(String tag, String msg, Throwable tr) {
            write('w', tag, msg, tr);
        }

        /**
         * W.
         *
         * @param tag the tag
         * @param msg the msg
         */
        public void w(String tag, String msg) {
            write('w', tag, msg);
        }

        /**
         * W.
         *
         * @param msg the msg
         */
        public void w(String msg) {
            write('w', msg);
        }

        private void write(char lvl, String message) {
            String tag;
            if (mTag == null)
                tag = TAG;
            else
                tag = mTag;
            write(lvl, tag, message);
        }

        private void write(char lvl, String tag, String message) {
            write(lvl, tag, message, null);
        }

        private void write(char lvl, String tag, String message, Throwable tr) {
            if (tag == null) {
                write(lvl, message);
                return;
            }
            LogEntry log = new LogEntry(lvl, tag, Thread.currentThread().getName(), message, tr);
            sendWriteMessage(log);
        }
    }
}


