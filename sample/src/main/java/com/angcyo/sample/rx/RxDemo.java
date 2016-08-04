package com.angcyo.sample.rx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
//        RxErrorOperator.catchDemo();
        RxErrorOperator.retryDemo();
    }

    /**
     * 辅助操作符
     */
    public static void assistOperator() {
//        RxAssistOperator.delayDemo();
//        RxAssistOperator.doDemo();
//        RxAssistOperator.timeIntervalDemo();
//        RxAssistOperator.timeoutDemo();
//        RxAssistOperator.timestampDemo();
//        RxAssistOperator.usingDemo();
        RxAssistOperator.toDemo();
    }

    /**
     * 条件操作符
     */
    public static void conditionalOperator() {
//        RxConditionalOperator.allDemo();
//        RxConditionalOperator.ambDemo();
        RxConditionalOperator.containsDemo();
    }

    /**
     * 算术操作符, 属于 Rx-Math 的内容
     */
    public static void mathOperator() {
//        RxMathOperator.countDemo();
//        RxMathOperator.concatDemo();
        RxMathOperator.reduceDemo();
    }

    /**
     * 连接操作符
     */
    public static void connectOperator() {
//        RxConnectOperator.publishDemo();
//        RxConnectOperator.refCountDemo();
        RxConnectOperator.replayDemo();
    }

    public static void rxDelayTest() {
        Observable.just(1).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                log("----" + integer);
                return 2;
            }
        }).delay(2, TimeUnit.SECONDS).observeOn(Schedulers.newThread()).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                log("----" + integer);
                return 3;
            }
        }).delay(2, TimeUnit.SECONDS).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                log("----" + integer);
                return 4;
            }
        }).subscribe();

//        Observable.just(1111).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                log("----" + integer);
//                return 2222;
//            }
//        }).delay(2, TimeUnit.SECONDS, Schedulers.newThread()).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                log("----" + integer);
//                return 3333;
//            }
//        }).delay(2, TimeUnit.SECONDS, Schedulers.newThread()).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                log("----" + integer);
//                return 4444;
//            }
//        }).delay(2, TimeUnit.SECONDS, Schedulers.newThread()).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                log("----" + integer);
//                return 555;
//            }
//        }).subscribe();
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
