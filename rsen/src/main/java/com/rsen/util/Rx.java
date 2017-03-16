package com.rsen.util;

import android.util.Log;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by robi on 2016-04-21 15:41.
 */
public class Rx {

    public static final Observable.Transformer<T, T> ioSchedulersTransformer = new Observable.Transformer<T, T>() {
        @Override
        public Observable<T> call(Observable<T> tObservable) {
            return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
    };
    public static final Observable.Transformer<T, T> newThreadSchedulersTransformer = new Observable.Transformer<T, T>() {
        @Override
        public Observable<T> call(Observable<T> tObservable) {
            return tObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static final <T> Observable.Transformer<T, T> applyNewThreadSchedulers() {
        return (Observable.Transformer<T, T>) newThreadSchedulersTransformer;
    }

    public static final <T> Observable.Transformer<T, T> applyIOSchedulers() {
        return (Observable.Transformer<T, T>) ioSchedulersTransformer;
    }

    public static <T> Observable.Transformer<T, T> normalSchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T, R> Subscription base(T t, Func1<? super T, ? extends R> func, final Action1<? super R> onNext) {
        return Observable.just(t).map(func).compose(applyIOSchedulers()).subscribe(onNext, throwable -> Log.e("Rx", "base: ", throwable));
    }

    public static <T, R> Subscription base(T t, Func1<? super T, ? extends R> func, final Action1<? super R> onNext, Scheduler scheduler) {
        return scheduler == Schedulers.newThread() ?
                Observable.just(t).map(func).compose(applyNewThreadSchedulers()).subscribe(onNext, throwable -> Log.e("Rx", "base: ", throwable))
                :
                Observable.just(t).map(func).compose(applyIOSchedulers()).subscribe(onNext, throwable -> Log.e("Rx", "base: ", throwable));
    }

    public static <T, R> Subscription base(T t, Func1<? super T, ? extends R> func) {
        return Observable.just(t).map(func).compose(applyIOSchedulers()).subscribe();
    }

    public static <T, R> Subscription base(T t, Func1<? super T, ? extends R> func, Scheduler scheduler) {
        return scheduler == Schedulers.newThread() ?
                Observable.just(t).map(func).compose(applyNewThreadSchedulers()).subscribe()
                :
                Observable.just(t).map(func).compose(applyIOSchedulers()).subscribe();
    }

    public static <R> Subscription base(Func1<String, ? extends R> func, final Action1<? super R> onNext) {
        return base("-", func, onNext);
    }

    public static <R> Subscription base(Func1<String, ? extends R> func, final Action1<? super R> onNext, Scheduler scheduler) {
        return base("-", func, onNext, scheduler);
    }

    public static <R> Subscription base(Func1<String, ? extends R> func) {
        return base("-", func);
    }

    public static <R> Subscription base(Func1<String, ? extends R> func, Scheduler scheduler) {
        return base("-", func, scheduler);
    }
}
