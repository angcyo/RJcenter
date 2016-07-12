package com.angcyo.sample.rx;

import rx.Observable;
import rx.Subscriber;
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
        Observable
                //此方法的执行线程,由subscribeOn指定,且只有最后一次有效
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            if (!subscriber.isUnsubscribed()) {
                                log(getMethodName());
                                subscriber.onNext("create call");
//                        throw new IllegalArgumentException("异常测试");
                            }
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .observeOn(Schedulers.newThread())//决定之后的观察在什么线程执行
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        log("map " + getMethodName());
                        return "s";
                    }
                })
                .observeOn(Schedulers.newThread())//决定之后的观察在什么线程执行
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        log("map 2 " + getMethodName());
                        return "s 2";
                    }
                })
                .observeOn(Schedulers.newThread())//决定之后的观察在什么线程执行
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        log(getMethodName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        log(getMethodName());
                    }

                    @Override
                    public void onNext(String s) {
                        log(getMethodName());
                    }
                });
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
