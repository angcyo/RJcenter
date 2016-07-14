package com.angcyo.sample.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;

/**
 * Rx 结合操作符
 * <p>
 * Created by angcyo on 2016-07-14.
 */
@SuppressWarnings("unchecked")
public class RxCombineOperator {
    public static void combineDemo() {
//        01:12:38 292 main:1->call 1 2 3 4
//        01:12:38 294 main:1->onNext 10
//        01:12:38 294 main:1->onCompleted
        Observable.combineLatest(
                Observable.just(1),
                Observable.just(2),
                Observable.just(3),
                Observable.just(4),
                new Func4<Integer, Integer, Integer, Integer, String>() {
                    @Override
                    public String call(Integer integer, Integer integer2, Integer integer3, Integer integer4) {
                        RxDemo.log(RxDemo.getMethodName() + " " + integer + " " + integer2 + " " + integer3 + " " + integer4);
                        return integer + integer2 + integer3 + integer4 + "";
                    }
                }).subscribe(new RxCreateOperator.Sub());
    }

    public static void joinDemo() {
        //未测试通过
        Observable.just(1).join(Observable.just(2), new Func1<Integer, Observable<Object>>() {
            @Override
            public Observable<Object> call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " left " + integer);
                return Observable.just(integer);
            }
        }, new Func1<Integer, Observable<Object>>() {
            @Override
            public Observable<Object> call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " right " + integer);
                return Observable.just(22);
            }
        }, new Func2<Integer, Integer, Object>() {
            @Override
            public Object call(Integer integer, Integer integer2) {
                RxDemo.log(RxDemo.getMethodName() + " result");
                return "";
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void startWithDemo() {
//        01:24:52 750 main:1->onNext 100
//        01:24:52 751 main:1->onNext 200
//        01:24:52 752 main:1->onNext 300
//        01:24:52 753 main:1->onNext 400
//        01:24:52 753 main:1->onNext 500
//        01:24:52 753 main:1->onNext 600
//        01:24:52 756 main:1->onNext 1
//        01:24:52 759 main:1->onNext 2
//        01:24:52 760 main:1->onNext 3
//        01:24:52 761 main:1->onNext 4
//        01:24:52 761 main:1->onNext 5
//        01:24:52 763 main:1->onCompleted
        //在发射数据之前, 先发射一组数据
        Observable.range(1, 5).startWith(100, 200, 300, 400, 500, 600).subscribe(new RxCreateOperator.Sub());
    }

    public static void mergeDemo() {
//        01:28:07 350 main:1->onNext 1
//        01:28:07 352 main:1->onNext 2
//        01:28:07 353 main:1->onNext 3
//        01:28:07 395 main:1->onNext 4
//        01:28:07 413 main:1->onCompleted
        //将多个Observables的输出合并，就好像它们是一个单个的Observable一样。..
        Observable.merge(Observable.just(1), Observable.just(2), Observable.just(3), Observable.just(4)).subscribe(new RxCreateOperator.Sub());
    }

    public static void switchDemo() {
//        Observable.range(1, 10).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer);
//                return integer;
//            }
//        }).switchOnNext(Observable.just(Observable.just(1000))).subscribe(new RxCreateOperator.Sub());

//        10:47:26 353 main:1->onNext 1
//        10:47:26 353 main:1->onNext 2
//        10:47:26 353 main:1->onNext 3
//        10:47:26 354 main:1->onNext 4
//        10:47:26 354 main:1->onNext 5
//        10:47:26 354 main:1->onNext 6
//        10:47:26 354 main:1->onNext 7
//        10:47:26 354 main:1->onNext 8
//        10:47:26 355 main:1->onNext 9
//        10:47:26 355 main:1->onNext 10
//        10:47:26 355 main:1->onCompleted
        Observable.switchOnNext(Observable.just(Observable.range(1, 10))).subscribe(new RxCreateOperator.Sub());
    }

    public static void zipDemo() {
//        11:00:25 176 RxComputationScheduler-1:15->call 1 0
//        11:00:25 176 RxComputationScheduler-1:15->onNext 1
//        11:00:26 162 RxComputationScheduler-1:15->call 2 1
//        11:00:26 162 RxComputationScheduler-1:15->onNext 3
//        11:00:34 163 RxComputationScheduler-1:15->onCompleted

        //它只发射与发射数据项最少的那个Observable一样多的数据。
        Observable.zip(Observable.range(1, 10), Observable.interval(1, TimeUnit.SECONDS).take(2), new Func2<Integer, Long, Long>() {
            @Override
            public Long call(Integer integer, Long aLong) {
                RxDemo.log(RxDemo.getMethodName() + " " + integer + " " + aLong);
                return integer + aLong;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }
}
