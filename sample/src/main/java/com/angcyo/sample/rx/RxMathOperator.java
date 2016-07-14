package com.angcyo.sample.rx;

import rx.Observable;
import rx.functions.Func2;

/**
 * Rx 算术操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Mathematical-and-Aggregate-Operators.html
 * <p>
 * 这个操作符不包含在RxJava核心模块中，它属于不同的rxjava-math模块。它被实现为四个操作符：averageDouble, averageFloat, averageInteger, averageLong。
 * Created by angcyo on 2016-07-15.
 */
@SuppressWarnings("unchecked")
public class RxMathOperator {
    public static void countDemo() {
        //返回发射数据的数量
//        12:42:22 807 main:1->onNext 100
//        12:42:22 807 main:1->onCompleted
        Observable.range(1, 100).count().subscribe(new RxCreateOperator.Sub());
    }

    public static void concatDemo() {
        //Merge操作符也差不多，它结合两个或多个Observable的发射物，但是数据可能交错，而Concat不会让多个Observable的发射物交错。
        Observable.concat(Observable.just(1), Observable.just(2), Observable.just(3)).subscribe(new RxCreateOperator.Sub());
    }

    public static void reduceDemo() {

//        12:48:42 934 main:1->call 1 2
//        12:48:42 935 main:1->call 3 3
//        12:48:42 935 main:1->call 6 4
//        12:48:42 935 main:1->call 10 5
//        12:48:42 936 main:1->call 15 6
//        12:48:42 936 main:1->call 21 7
//        12:48:42 936 main:1->call 28 8
//        12:48:42 936 main:1->call 36 9
//        12:48:42 937 main:1->call 45 10
//        12:48:42 938 main:1->onNext 55
//        12:48:42 938 main:1->onCompleted
        Observable.range(1, 10).reduce(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                RxDemo.log(RxDemo.getMethodName() + " " + integer + " " + integer2);
                return integer + integer2;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }
}
