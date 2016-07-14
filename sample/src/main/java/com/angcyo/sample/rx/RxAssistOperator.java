package com.angcyo.sample.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Rx 辅助操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Observable-Utility-Operators.html
 * Created by angcyo on 2016-07-14.
 */
@SuppressWarnings("unchecked")
public class RxAssistOperator {
    public static void delayDemo() {
        RxDemo.log(RxDemo.getMethodName() + " ----start");
        Observable.range(1, 100).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " " + integer);
                return integer;
            }
        }).delay(5, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());
    }
}
