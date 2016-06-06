package com.rsen.util;

import android.util.Log;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by robi on 2016-04-21 15:41.
 */
public class Rx {

    public static <T, R> void base(T t, Func1<? super T, ? extends R> func, final Action1<? super R> onNext) {
        Observable.just(t).map(func).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> Log.e("Rx", "base: ", throwable));
    }

    public static <T, R> void base(T t, Func1<? super T, ? extends R> func, final Action1<? super R> onNext, Scheduler scheduler) {
        Observable.just(t).map(func).subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> Log.e("Rx", "base: ", throwable));
    }

    public static <T, R> void base(T t, Func1<? super T, ? extends R> func) {
        Observable.just(t).map(func).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public static <T, R> void base(T t, Func1<? super T, ? extends R> func, Scheduler scheduler) {
        Observable.just(t).map(func).subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public static <R> void base(Func1<String, ? extends R> func, final Action1<? super R> onNext) {
        base("-", func, onNext);
    }

    public static <R> void base(Func1<String, ? extends R> func, final Action1<? super R> onNext, Scheduler scheduler) {
        base("-", func, onNext, scheduler);
    }

    public static <R> void base(Func1<String, ? extends R> func) {
        base("-", func);
    }

    public static <R> void base(Func1<String, ? extends R> func, Scheduler scheduler) {
        base("-", func, scheduler);
    }
}
