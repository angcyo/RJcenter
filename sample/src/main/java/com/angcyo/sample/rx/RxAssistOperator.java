package com.angcyo.sample.rx;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

/**
 * Rx 辅助操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Observable-Utility-Operators.html
 * Created by angcyo on 2016-07-14.
 */
@SuppressWarnings("unchecked")
public class RxAssistOperator {
    private static int num = 0;

    public static void delayDemo() {
        RxDemo.log(RxDemo.getMethodName() + " ----start");
        //此方式, 数据依旧会发射, 但是 onNext 方法会延时执行, num = 0
//        Observable.range(1, 10).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer + " " + num);
//                return integer;
//            }
//        }).delay(5, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());

        //此方式, 会延时发射数据, 但是 onNext会马上执行, num 会在执行的时候读取
//        Observable.range(1, 10).delay(5, TimeUnit.SECONDS).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer + " " + num);
//                return integer;
//            }
//        }).subscribe(new RxCreateOperator.Sub());

        //根据条件,决定是否延迟
//        Observable.range(1, 10).delay(new Func1<Integer, Observable<Object>>() {
//            @Override
//            public Observable<Object> call(Integer integer) {
//                if (integer == 2) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                RxDemo.log(RxDemo.getMethodName() + " " + integer);
//                return Observable.just(integer);
//            }
//        }).subscribe(new RxCreateOperator.Sub());

        //结果是一样的.
//        Observable.range(1, 10).delay(10, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());
//        Observable.range(1, 10).delaySubscription(10, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());

        //没效果
        Observable.range(1, 10).delaySubscription(Observable.just(1000)).subscribe(new RxCreateOperator.Sub());
        num = 5;
    }

    public static void doDemo() {
        //注册一个动作作为原始Observable生命周期事件的一种占位符

        //10:44:18 491 RxComputationScheduler-1:12->call 0
//        10:44:18 492 RxComputationScheduler-1:12->onNext 0
//        10:44:19 467 RxComputationScheduler-1:12->call 1
//        10:44:19 468 RxComputationScheduler-1:12->onNext 1
//        10:44:20 469 RxComputationScheduler-1:12->call 2
//        10:44:20 469 RxComputationScheduler-1:12->onNext 2
//        10:44:21 464 RxComputationScheduler-1:12->call 3
//        10:44:21 464 RxComputationScheduler-1:12->onNext 3
//        10:44:22 468 RxComputationScheduler-1:12->call 4
//        10:44:22 468 RxComputationScheduler-1:12->onNext 4
//        10:44:22 468 RxComputationScheduler-1:12->call null   //注意此处
//        10:44:22 469 RxComputationScheduler-1:12->onCompleted
//        Observable.interval(1, TimeUnit.SECONDS).take(5).doOnEach(new Action1<Notification<? super Long>>() {
//            @Override
//            public void call(Notification<? super Long> notification) {
//                RxDemo.log(RxDemo.getMethodName() + " " + notification.getValue());
//            }
//        }).subscribe(new RxCreateOperator.Sub());


//        10:46:23 558 RxComputationScheduler-1:12->call 0
//        10:46:23 560 RxComputationScheduler-1:12->onNext 0
//        10:46:24 520 RxComputationScheduler-1:12->call 1
//        10:46:24 521 RxComputationScheduler-1:12->onNext 1
//        10:46:25 520 RxComputationScheduler-1:12->call 2
//        10:46:25 521 RxComputationScheduler-1:12->onNext 2
//        10:46:26 519 RxComputationScheduler-1:12->call 3
//        10:46:26 519 RxComputationScheduler-1:12->onNext 3
//        10:46:27 518 RxComputationScheduler-1:12->call 4
//        10:46:27 518 RxComputationScheduler-1:12->onNext 4
//        10:46:27 518 RxComputationScheduler-1:12->onCompleted
//        Observable.interval(1, TimeUnit.SECONDS).take(5).doOnNext(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
//                RxDemo.log(RxDemo.getMethodName() + " " + aLong);
//            }
//        }).subscribe(new RxCreateOperator.Sub());

        //会在subscribe之前调用
//        Observable.interval(1, TimeUnit.SECONDS).take(5).doOnSubscribe(new Action0() {
//            @Override
//            public void call() {
//                RxDemo.log(RxDemo.getMethodName() + " onSubscribe");
//            }
//        }).subscribe(new RxCreateOperator.Sub());

        //会在onCompleted之前调用
//        Observable.interval(1, TimeUnit.SECONDS).take(5).doOnCompleted(new Action0() {
//            @Override
//            public void call() {
//                RxDemo.log(RxDemo.getMethodName() + " onCompleted");
//            }
//        }).subscribe(new RxCreateOperator.Sub());


        //11:05:11 326 RxComputationScheduler-1:12->onNext 0
//        11:05:12 300 RxComputationScheduler-1:12->onNext 1
//        11:05:13 304 RxComputationScheduler-1:12->onNext 2
//        11:05:14 304 RxComputationScheduler-1:12->onNext 3
//        11:05:15 303 RxComputationScheduler-1:12->onNext 4
//        11:05:15 303 RxComputationScheduler-1:12->onCompleted
//        11:05:15 303 RxComputationScheduler-1:12->call doAfterTerminate
        Observable.interval(1, TimeUnit.SECONDS).take(5).doAfterTerminate(new Action0() {
            @Override
            public void call() {
                RxDemo.log(RxDemo.getMethodName() + " doAfterTerminate");
            }
        }).serialize().subscribe(new RxCreateOperator.Sub());
    }

    public static void timeIntervalDemo() {
        //可以得到2个发射物之间的时间间隔

//        11:43:21 203 main:1->call 1 1
//        11:43:21 204 main:1->onNext TimeInterval [intervalInMilliseconds=1, value=1]
//        11:43:21 204 main:1->call 21 2
//        11:43:21 204 main:1->onNext TimeInterval [intervalInMilliseconds=21, value=2]
//        11:43:21 205 main:1->call 1 3
        Observable.range(1, 10).timeInterval().filter(new Func1<TimeInterval<Integer>, Boolean>() {
            @Override
            public Boolean call(TimeInterval<Integer> integerTimeInterval) {
                RxDemo.log(RxDemo.getMethodName() + " " + integerTimeInterval.getIntervalInMilliseconds()
                        + " " + integerTimeInterval.getValue());
                return true;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void timeoutDemo() {
        //如果5秒之内,还没有数据发射,就调用onError,结束数据发射.
        Observable.interval(1, TimeUnit.SECONDS).delay(10, TimeUnit.SECONDS).timeout(5, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());
    }

    public static void timestampDemo() {
        //可以得到发射数据的时间

//        11:42:06 868 main:1->onNext Timestamped(timestampMillis = 1468510926867, value = 5)
//        11:42:06 868 main:1->call 1468510926868 6
        Observable.range(1, 10).timestamp().filter(new Func1<Timestamped<Integer>, Boolean>() {
            @Override
            public Boolean call(Timestamped<Integer> integerTimestamped) {
                RxDemo.log(RxDemo.getMethodName() + " " + integerTimestamped.getTimestampMillis() + " " + integerTimestamped.getValue());
                return true;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void usingDemo() {
        //未测出效果
        Observable.using(new Func0<String>() {
            @Override
            public String call() {
                RxDemo.log(RxDemo.getMethodName() + " onCall");
                return null;
            }
        }, new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String s) {
                RxDemo.log(RxDemo.getMethodName() + " func1 " + s);
                return Observable.just(s + " call");
            }
        }, new Action1<String>() {
            @Override
            public void call(String s) {
                RxDemo.log(RxDemo.getMethodName() + " onAction " + s);
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void toDemo() {

//        11:52:43 562 main:1->call list size =  10
//        11:52:43 564 main:1->onNext 10000
//        11:52:43 564 main:1->onCompleted
        Observable.range(1, 10).toList().map(new Func1<List<Integer>, Integer>() {
            @Override
            public Integer call(List<Integer> integers) {
                RxDemo.log(RxDemo.getMethodName() + " list size =  " + integers.size());
                return 10000;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }
}
