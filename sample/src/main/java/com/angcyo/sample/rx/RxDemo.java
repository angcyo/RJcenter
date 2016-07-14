package com.angcyo.sample.rx;

import java.text.SimpleDateFormat;
import java.util.Date;

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
//        RxCreateOperator.repeatDemo();
//        RxCreateOperator.deferDemo();
//        RxCreateOperator.emptyDemo();
//        RxCreateOperator.intervalDemo();
        RxCreateOperator.rangeDemo();
    }

    /**
     * 变换操作符
     */
    public static void flatOperator() {
//        RxFlatOperator.mapDemo();
//        RxFlatOperator.scanDemo();
//        RxFlatOperator.bufferDemo();
//        RxFlatOperator.flatMapDemo();
//        RxFlatOperator.groupByDemo();
        RxFlatOperator.windowDemo();
    }

    /**
     * 过滤操作符
     */
    public static void filterOperator() {
//        RxFilterOperator.debounceDemo();
//        RxFilterOperator.distinctDemo();
//        RxFilterOperator.elementAtDemo();
//        RxFilterOperator.filterDemo();
//        RxFilterOperator.firstDemo();
//        RxFilterOperator.singleDemo();
//        RxFilterOperator.ignoreElementsDemo();
//        RxFilterOperator.lastDemo();
//        RxFilterOperator.sampleDemo();
//        RxFilterOperator.skipDemo();
        RxFilterOperator.takeDemo();
    }

    /**
     * 结合操作符
     */
    public static void combineOperator() {
//        RxCombineOperator.combineDemo();
//        RxCombineOperator.joinDemo();
//        RxCombineOperator.startWithDemo();
//        RxCombineOperator.mergeDemo();
//        RxCombineOperator.switchDemo();
        RxCombineOperator.zipDemo();
    }

    /**
     * 错误操作符
     */
    public static void errorOperator() {
        RxErrorOperator.catchDemo();
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
        System.out.println(new SimpleDateFormat("hh:mm:ss SSS").format(new Date()) + " " + getThreadName() + ":" + getThreadId() + "->" + log);
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
