package com.angcyo.sample.rx;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Rx 创建操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Creating-Observables.html
 * Created by robi on 2016-07-13 18:08.
 */
@SuppressWarnings("unchecked")
public class RxCreateOperator {
    static long count = 0;

    /**
     * create方法测试
     */
    public static void createDemo() {
        Observable
                //此方法的执行线程,由subscribeOn指定,且只有最后一次有效
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            if (!subscriber.isUnsubscribed()) {
                                RxDemo.log(RxDemo.getMethodName());
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
                        RxDemo.log("map " + RxDemo.getMethodName());
                        return "s";
                    }
                })
                .observeOn(Schedulers.newThread())//决定之后的观察在什么线程执行
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        RxDemo.log("map 2 " + RxDemo.getMethodName());
                        return "s 2";
                    }
                })
                .observeOn(Schedulers.newThread())//决定之后的观察在什么线程执行
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Sub());
    }

    /**
     * just方法测试
     */
    public static void justDemo() {
        Observable.just("a", "b", "c")
//                .observeOn(Schedulers.computation())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        RxDemo.log(RxDemo.getMethodName() + " computation");
                        return "d";
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        RxDemo.log(RxDemo.getMethodName() + " io");
                        return "d";
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Sub());
    }

    public static void fromDemo() {
        Observable.from(new Future<String>() {
            @Override
            public boolean cancel(boolean b) {
                RxDemo.log(RxDemo.getMethodName());
                return false;
            }

            @Override
            public boolean isCancelled() {
                RxDemo.log(RxDemo.getMethodName());
                return false;
            }

            @Override
            public boolean isDone() {
                RxDemo.log(RxDemo.getMethodName());
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                RxDemo.log(RxDemo.getMethodName());
                return null;
            }

            @Override
            public String get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                RxDemo.log(RxDemo.getMethodName());
                return null;
            }
        }).observeOn(Schedulers.newThread()).subscribe(new Sub());

        Observable.from(new Integer[]{1, 2, 3}).subscribe(new Sub());
    }

    public static void repeatDemo() {
        //重复发射多少次数据
        //当 .repeat() 接收到 .onCompleted() 事件后触发重订阅。
        //当 .retry() 接收到 .onError() 事件后触发重订阅。
//        Observable.just("a", "b", "c", "d").repeat(3, Schedulers.computation()).map(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                RxDemo.log(RxDemo.getMethodName() + "--" + s);
//                return "" + System.currentTimeMillis();
//            }
//        }).subscribe(new Sub());

//        10:01:20 940 main:1->call
//        10:01:20 947 main:1->onNext a
//        10:01:20 948 main:1->onNext b
//        10:01:20 948 main:1->onNext c
//        10:01:20 948 main:1->onNext d
//        10:01:20 950 main:1->call 0
        //在什么情况下重复发射数据
        Observable.just("a", "b", "c", "d")
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        RxDemo.log(RxDemo.getMethodName());
                        return observable.filter(new Func1<Void, Boolean>() {
                            @Override
                            public Boolean call(Void aVoid) {
                                RxDemo.log(RxDemo.getMethodName() + " " + count);
                                count++;
                                return count < 1;
                            }
                        });
                    }
                })
                .subscribe(new Sub());
    }

    public static void deferDemo() {
        //just操作符是在创建Observable就进行了赋值操作，而defer是在订阅者订阅时才创建Observable，此时才进行真正的赋值操作
        //defer 操作符, 中的变量会在订阅的时候,才开始赋值
        Observable.defer(new Func0<Observable<Object>>() {
            @Override
            public Observable<Object> call() {
                RxDemo.log(RxDemo.getMethodName());
                return Observable.just("angcyo");
            }
        }).subscribe(new Sub());
    }

    public static void emptyDemo() {
        //创建一个不发射任何数据但是正常终止的Observable
        Observable.empty().map(new Func1<Object, String>() {
            @Override
            public String call(Object o) {
                RxDemo.log(RxDemo.getMethodName());
                return "--";
            }
        }).subscribe(new Sub());

        //创建一个不发射数据也不终止的Observable
//        Observable.never().subscribe(new Sub());

        //创建一个不发射数据以一个错误终止的Observable
//        Observable.error(new Throwable("err")).subscribe(new Sub());
    }

    public static void intervalDemo() {
        //每次间隔2秒, 发射一次数据.  数据源是 次数0,1,2,3,4,...(一开始也会等待2秒,才开始)
        RxDemo.log("-------------start");
        Observable.interval(2, TimeUnit.SECONDS).subscribe(new Sub());

        //延迟3秒开始, 间隔2秒发射一次数据
//        Observable.interval(3, 2, TimeUnit.SECONDS, Schedulers.newThread()).subscribe(new Sub());
    }

    public static void rangeDemo() {
        //从0开始, 发射10个数据
        Observable.range(0, 10).subscribe(new Sub());

        Observable.range(2, 2, Schedulers.newThread()).subscribe(new Sub());
    }

    /**
     * 测试专用
     */
    public static class Sub extends Subscriber {

        @Override
        public void onCompleted() {
            RxDemo.log(RxDemo.getMethodName());
        }

        @Override
        public void onError(Throwable e) {
            RxDemo.log(RxDemo.getMethodName());
        }

        @Override
        public void onNext(Object o) {
            RxDemo.log(RxDemo.getMethodName() + " " + o.toString());
        }
    }
}
