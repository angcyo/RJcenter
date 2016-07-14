package com.angcyo.sample.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Rx 连接操作符
 * https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Connectable-Observable-Operators.html
 * Created by angcyo on 2016-07-15.
 */
@SuppressWarnings("unchecked")
public class RxConnectOperator {
    public static void publishDemo() {
        //将普通的Observable转换为可连接的Observable,
        // 可连接的Observable可以有多个订阅者, 并且只有调用了connect之后,才开始发射数据.
        // 这样的话, 可以让所有订阅者订阅之后, 调用connect, 开始发射数据.
        //这样的代码,结果是没有任何输出
        Observable.range(1, 10).publish().map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " " + integer);
                return integer;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void refCountDemo() {
        //让一个可连接的Observable行为像普通的Observable, 这2个操作符中合了;
        Observable.range(1, 10).publish().refCount().map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                RxDemo.log(RxDemo.getMethodName() + " " + integer);
                return integer;
            }
        }).subscribe(new RxCreateOperator.Sub());
    }

    public static void replayDemo() {
        //没输出
//        Observable.range(1, 10).replay().subscribe(new RxCreateOperator.Sub());

        //没有输出
//        Observable.range(1, 10).publish().replay(3).subscribe(new RxCreateOperator.Sub());

        //没有输出
//        Observable.range(1, 10).replay(2, TimeUnit.SECONDS).subscribe(new RxCreateOperator.Sub());

        //没有输出
//        Observable.range(1, 10).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer);
//                return integer;
//            }
//        }).replay().subscribe(new RxCreateOperator.Sub());

        //没有输出
//        Observable.just(1).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer);
//                return integer;
//            }
//        }).replay().connect();

        //有数据输出了.
//        Observable.just(1).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                RxDemo.log(RxDemo.getMethodName() + " " + integer);
//                return integer;
//            }
//        }).replay().autoConnect().subscribe(new RxCreateOperator.Sub());

        //和普通的观察者没有发现什么区别
//        Observable.range(1, 10).publish().autoConnect().subscribe(new RxCreateOperator.Sub());

//        Observable.just(1).replay(3).autoConnect().subscribe(new RxCreateOperator.Sub());

        Observable.just(2).replay(1, TimeUnit.SECONDS).autoConnect().subscribe(new RxCreateOperator.Sub());
    }

}
