package com.angcyo.sample.rx;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by angcyo on 2016-07-13.
 */
public class RxDemo {
    /**
     * 创建操作符
     */
    public static void createOperator() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    log(getMethodName());
                }
            }
        }).subscribe(new Subscriber<String>() {
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
        Log.i("angcyo->" + getThreadName() + ":" + getThreadId(), log);
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
