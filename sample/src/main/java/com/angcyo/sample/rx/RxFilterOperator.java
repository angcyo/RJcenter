package com.angcyo.sample.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Rx 过滤操作符
 * Created by angcyo on 2016-07-13.
 */
@SuppressWarnings("unchecked")
public class RxFilterOperator {
    public static void debounceDemo() {
        //过滤2秒之内就发射了的数据
        Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        final int time = (int) (aLong % 3);
                        RxDemo.log(RxDemo.getMethodName() + " " + time);
                        try {
                            Thread.sleep(time * 2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return aLong + 100;
                    }
                })
                .debounce(2, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());
    }

    public static void distinctDemo() {
        //过滤掉已经发射过的数据
        Observable.just("a", "b", "a", "c", "b").distinct().subscribe(new RxCreateOperator.Sub());
    }

    public static void elementAtDemo() {
        //只发射位置为2的数据
        Observable.interval(1, TimeUnit.SECONDS).elementAt(2).subscribe(new RxCreateOperator.Sub());
    }

    public static void filterDemo() {
        //过滤不满足条件的数据, 只发射大于5的数据
        Observable.interval(1, TimeUnit.SECONDS).filter(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return aLong > 5;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void firstDemo() {
        //只发射第一个数据
//        Observable.interval(1, TimeUnit.SECONDS).first().subscribe(new RxCreateOperator.Sub());

        //发射第一个数据, 如果没有,则发射默认的数据
//        Observable.just(1, 2, 3, 4).firstOrDefault(100).subscribe(new RxCreateOperator.Sub());

        //发射满足条件的第一个数据, 如果没有, 则发射默认的数据
//        Observable.just(1, 2, 3, 4).firstOrDefault(100, new Func1<Integer, Boolean>() {
//            @Override
//            public Boolean call(Integer integer) {
//                return integer > 5;
//            }
//        }).subscribe(new RxCreateOperator.Sub());

        //抛出异常
//        Observable.empty().first().subscribe(new RxCreateOperator.Sub());

        //如果第一条数据为空,则使用默认的数据发射.
        Observable.empty().firstOrDefault(100).subscribe(new RxCreateOperator.Sub());
    }

    public static void singleDemo() {
        //如果发射的数据不等于1条, 则调用onError, 否则调用onNext, onCompleted
//        Observable.just(1).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer);
//                return integer;
//            }
//        }).single().subscribe(new RxCreateOperator.Sub());

        //如果发射的数据为空,则使用默认的数据发射
//        Observable.empty().singleOrDefault(100).subscribe(new RxCreateOperator.Sub());

        //发射的数据为空, 会抛出异常.
        Observable.empty().single().subscribe(new RxCreateOperator.Sub());
    }

    public static void ignoreElementsDemo() {
        //不会结束发射数据, 没有输出log
        Observable.interval(1, TimeUnit.SECONDS).ignoreElements().subscribe(new RxCreateOperator.Sub());

        //只允许 onError,  onCompleted通过, onNext 不会被调用.
        Observable.just(1).ignoreElements().subscribe(new RxCreateOperator.Sub());
    }

    public static void lastDemo() {
        //不会结束发射数据, 没有输出log
        Observable.interval(1, TimeUnit.SECONDS).last().subscribe(new RxCreateOperator.Sub());

        //只发射最后一个数据
        Observable.just(1, 2, 3, 4).last().subscribe(new RxCreateOperator.Sub());

        //没有最后一条数据,会抛出异常
        Observable.empty().last().subscribe(new RxCreateOperator.Sub());

        //没有最后一条数据,使用默认数据发射
        Observable.empty().lastOrDefault(1000).subscribe(new RxCreateOperator.Sub());

        //发射满足条件的最后一个数据
        Observable.just(1, 2, 3, 4).last(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer == 2;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void sampleDemo() {
        RxDemo.log(RxDemo.getMethodName() + "-----start");
        //3秒取样一次, 发射数据
        Observable.interval(1, TimeUnit.SECONDS).sample(3, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());
    }

    public static void skipDemo() {
        //跳过之前的2个数据
//        Observable.interval(1, TimeUnit.SECONDS).skip(2).subscribe(new RxCreateOperator.Sub());

        //跳过后面的2歌数据
        Observable.just(1, 2, 3, 4, 5, 6).skipLast(2).subscribe(new RxCreateOperator.Sub());
    }

    public static void takeDemo() {
        //取前面3个数据
        Observable.interval(1, TimeUnit.SECONDS).take(3).subscribe(new RxCreateOperator.Sub());

        Observable.empty().take(10).subscribe(new RxCreateOperator.Sub());

        //取后面的2个数据
        Observable.just(1, 2, 3, 4, 5).takeLast(2).subscribe(new RxCreateOperator.Sub());
    }
}
