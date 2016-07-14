package com.angcyo.sample.rx;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Rx 变换操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Transforming-Observables.html
 * Created by robi on 2016-07-13 20:46.
 */
@SuppressWarnings("unchecked")
public class RxFlatOperator {
    public static void mapDemo() {
        //对Observable发射的每一项数据应用一个函数，执行变换操作
        Observable.just("map").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                RxDemo.log(RxDemo.getMethodName() + " " + s);
                return 100;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new RxCreateOperator.Sub());
    }

    public static void scanDemo() {
        //连续地对数据序列的每一项应用一个函数，然后连续发射结果
        Observable.just(1, 2, 3, 4).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                //第一个参数:第一次发射的第一个数据, 之后是此方法返回的数据
                RxDemo.log(RxDemo.getMethodName() + " " + integer + " " + integer2);
                return 0;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void bufferDemo() {
        //每次打包2个数据, 每次从原来的第3个数据开始
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).buffer(2, 3).subscribe(new RxCreateOperator.Sub());

//        09:08:29 521 main:1->onNext [1, 2]
//        09:08:29 523 main:1->onNext [4, 5]
//        09:08:29 523 main:1->onNext [7, 8]
//        09:08:29 523 main:1->onNext [0]
//        09:08:29 524 main:1->onCompleted
//        09:08:29 527 main:1->call 1 2
//        09:08:29 527 main:1->onNext 3
//        09:08:29 527 main:1->call 4 5
//        09:08:29 528 main:1->onNext 9
//        09:08:29 529 main:1->call 7 8
//        09:08:29 529 main:1->onNext 15
//        09:08:29 532 main:1->onError
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).buffer(2, 3).map(new Func1<List<Integer>, Integer>() {
            @Override
            public Integer call(List<Integer> integers) {
                RxDemo.log(RxDemo.getMethodName() + " " + integers.get(0) + " " + integers.get(01));
                return integers.get(0) + integers.get(1);
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void flatMapDemo() {
        //FlatMap将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable

//        11:08:57 560 main:1->call 1
//        11:08:57 588 main:1->onNext 11
//        11:08:57 588 main:1->onNext 100
//        11:08:57 589 main:1->call 2
//        11:08:57 589 main:1->onNext 12
//        11:08:57 589 main:1->onNext 100
//        11:08:57 589 main:1->call 3
//        11:08:57 590 main:1->onNext 13
//        11:08:57 590 main:1->onNext 100
//        11:08:57 590 main:1->call 4
//        11:08:57 591 main:1->onNext 14
//        11:08:57 591 main:1->onNext 100
//        11:08:57 591 main:1->call 5
//        11:08:57 591 main:1->onNext 15
//        11:08:57 592 main:1->onNext 100
//        11:08:57 592 main:1->call 6
//        11:08:57 592 main:1->onNext 16
//        11:08:57 592 main:1->onNext 100
//        11:08:57 593 main:1->call 7
//        11:08:57 593 main:1->onNext 17
//        11:08:57 593 main:1->onNext 100
//        11:08:57 593 main:1->call 8
//        11:08:57 594 main:1->onNext 18
//        11:08:57 594 main:1->onNext 100
//        11:08:57 594 main:1->call 9
//        11:08:57 595 main:1->onNext 19
//        11:08:57 595 main:1->onNext 100
//        11:08:57 595 main:1->call 0
//        11:08:57 599 main:1->onNext 10
//        11:08:57 599 main:1->onNext 100
//        11:08:57 599 main:1->onCompleted
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).flatMap(new Func1<Integer, Observable<?>>() {
            @Override
            public Observable<?> call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " " + integer);
                return Observable.just(integer + 10, 100);
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void groupByDemo() {
        Observable.interval(1, TimeUnit.SECONDS).groupBy(new Func1<Long, Long>() {
            @Override
            public Long call(Long aLong) {
                RxDemo.log(RxDemo.getMethodName() + " " + aLong);
                return aLong;
            }
        }).take(10).subscribe(new Subscriber<GroupedObservable<Long, Long>>() {
            @Override
            public void onCompleted() {
                RxDemo.log(RxDemo.getMethodName());
            }

            @Override
            public void onError(Throwable e) {
                RxDemo.log(RxDemo.getMethodName());
            }

            @Override
            public void onNext(GroupedObservable<Long, Long> longLongGroupedObservable) {
                RxDemo.log(RxDemo.getMethodName());
                longLongGroupedObservable.subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        RxDemo.log(RxDemo.getMethodName() + " " + longLongGroupedObservable.getKey());
                    }

                    @Override
                    public void onError(Throwable e) {
                        RxDemo.log(RxDemo.getMethodName() + " " + longLongGroupedObservable.getKey());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        RxDemo.log(RxDemo.getMethodName() + " " + longLongGroupedObservable.getKey() + " " + aLong);
                    }
                });
            }
        });
    }

    public static void windowDemo() {

//        11:28:07 642 RxComputationScheduler-1:12->call
//        11:28:07 642 RxComputationScheduler-1:12->onNext 1468423687642
//        11:28:07 642 RxComputationScheduler-1:12->onNext next 0
//        11:28:08 618 RxComputationScheduler-1:12->onNext next 1
//        11:28:08 618 RxComputationScheduler-1:12->onCompleted
//        11:28:09 618 RxComputationScheduler-1:12->call
//        11:28:09 619 RxComputationScheduler-1:12->onNext 1468423689619
//        11:28:09 619 RxComputationScheduler-1:12->onNext next 2
//        11:28:10 617 RxComputationScheduler-1:12->onNext next 3
//        11:28:10 617 RxComputationScheduler-1:12->onCompleted
//        11:28:11 616 RxComputationScheduler-1:12->call
//        11:28:11 616 RxComputationScheduler-1:12->onNext 1468423691616
//        11:28:11 616 RxComputationScheduler-1:12->onNext next 4
//        11:28:12 616 RxComputationScheduler-1:12->onNext next 5
//        11:28:12 616 RxComputationScheduler-1:12->onCompleted
//        11:28:13 617 RxComputationScheduler-1:12->call
//        11:28:13 617 RxComputationScheduler-1:12->onNext 1468423693617
//        11:28:13 617 RxComputationScheduler-1:12->onNext next 6
//        11:28:14 616 RxComputationScheduler-1:12->onNext next 7
//        11:28:14 616 RxComputationScheduler-1:12->onCompleted
//        11:28:15 617 RxComputationScheduler-1:12->call
//        11:28:15 618 RxComputationScheduler-1:12->onNext 1468423695618
//        11:28:15 618 RxComputationScheduler-1:12->onNext next 8
//        11:28:16 619 RxComputationScheduler-1:12->onNext next 9
//        11:28:16 619 RxComputationScheduler-1:12->onCompleted
//        11:28:16 619 RxComputationScheduler-1:12->onCompleted
        Observable.interval(1, TimeUnit.SECONDS).take(10).window(2)
                .map(new Func1<Observable<Long>, Long>() {
                    @Override
                    public Long call(Observable<Long> longObservable) {
                        RxDemo.log(RxDemo.getMethodName());
                        longObservable.subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                RxDemo.log(RxDemo.getMethodName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxDemo.log(RxDemo.getMethodName());
                            }

                            @Override
                            public void onNext(Long aLong) {
                                RxDemo.log(RxDemo.getMethodName() + " next " + aLong);
                            }
                        });
                        return System.currentTimeMillis();
                    }
                })
                .subscribe(new RxCreateOperator.Sub());
    }
}
