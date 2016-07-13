package com.angcyo.sample.rx;

/**
 * Created by angcyo on 2016-07-13.
 */
public class RxDemo {
    /**
     * 创建操作符
     */
    public static void createOperator() {
//        RxCreateOperator.createDemo();
//        RxCreateOperator.justDemo();
//        RxCreateOperator.fromDemo();
        RxCreateOperator.repeatDemo();
    }

    public static String getMethodName() {
        Exception exception = new Exception();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        return stackTrace[1].getMethodName();
    }

    /**
     * Log.
     *
     * @param log the log
     */
    public static void log(String log) {
        System.out.println(getThreadName() + ":" + getThreadId() + "->" + log);
//        Log.i("angcyo->" + getThreadName() + ":" + getThreadId(), log);
    }

    /**
     * Gets thread name.
     *
     * @return the thread name
     */
    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * Gets thread id.
     *
     * @return the thread id
     */
    public static long getThreadId() {
        return Thread.currentThread().getId();
    }
}
