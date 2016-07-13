package com.angcyo.sample.rx;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Rx 变换操作符
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
                //第一个参数,第一次是发射的第一个数据, 之后是此方法返回的数据
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
}
