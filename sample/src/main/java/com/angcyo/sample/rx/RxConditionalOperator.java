package com.angcyo.sample.rx;

import rx.Observable;
import rx.functions.Func1;

/**
 * Rx 条件操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Conditional-and-Boolean-Operators.html
 * Created by angcyo on 2016-07-14.
 */
@SuppressWarnings("unchecked")
public class RxConditionalOperator {
    public static void allDemo() {

        //如果有一个数据发生了错误, 立马终止之后的数据发射.
        Observable.range(1, 10).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " onCall " + integer);
                return false;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void ambDemo() {
        //只会发射一个数据, 不管这个数据是否正常结束, 后面的数据都不发射.
        //不管发射的是一项数据还是一个onError或onCompleted通知。Amb将忽略和丢弃其它所有Observables的发射物。
        Observable.amb(Observable.just(1), Observable.just(2), Observable.just(3), Observable.just(4)).subscribe(new RxCreateOperator.Sub());
    }

    public static void containsDemo() {

        //判断数据中,是否包含给定的值
//        12:28:40 722 main:1->onNext false
//        12:28:40 723 main:1->onCompleted
//        Observable.range(1, 10).contains(22).subscribe(new RxCreateOperator.Sub());

        //isEmpty = false
//        Observable.range(1, 10).contains(22).isEmpty().subscribe(new RxCreateOperator.Sub());

        //isEmpty = true
//        Observable.empty().isEmpty().subscribe(new RxCreateOperator.Sub());

        //判断发射的数据中, 是否存在给定的值, 如果找到了, 则结束继续发射数据
        Observable.range(1, 10).exists(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + integer);
                if (integer == 50) {
                    return true;
                }
                return false;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }
}
