package com.angcyo.rxjavademo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rsen.util.L;
import com.rsen.util.T;

import java.net.URL;
import java.net.URLConnection;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                L.e("主线程id:" + Thread.currentThread().getId());
//                rxDemo();
//                rxDemo2();
//                rxDemo3();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        L.e("执行线程id:" + Thread.currentThread().getId());

//                        rxDemo4();
//                        rxDemo5();
                        rxDemo6();

                    }
                }).start();
            }
        });

        T.show(MainActivity.this, "onCreate");
    }

    private void rxDemo() {
        //1:定义一个 可以被观察的对象
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onCompleted();
            }
        });

        //等效以上写法
        Observable observable1 = Observable.just("Hello", "Hi", "Aloha");

        //等效以上写法
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable2 = Observable.from(words);

        //2:将可以被观察的对象,交给观察者执行
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                L.e("onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                L.e("onNext -- " + s);
            }
        });
    }

    private void rxDemo2() {
        Observable.just("Hello", "Hi", "Ok", "Rsen")
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("RSenL", "doOnNext thread id:" + Thread.currentThread().getId());
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e("RSenL", "doOnSubscribe thread id:" + Thread.currentThread().getId());
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        Log.e("RSenL", "doOnCompleted thread id:" + Thread.currentThread().getId());
                    }
                })
                .doOnEach(new Action1<Notification<? super String>>() {
                    @Override
                    public void call(Notification<? super String> notification) {
                        Log.e("RSenL", "doOnEach thread id:" + Thread.currentThread().getId());
                    }
                })
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e("RSenL", "doOnRequest thread id:" + Thread.currentThread().getId());
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        Log.e("RSenL", "doOnTerminate thread id:" + Thread.currentThread().getId());
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e("RSenL", "doOnUnsubscribe thread id:" + Thread.currentThread().getId());
                    }
                })

                .subscribeOn(Schedulers.io())
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("RSenL", "call -- " + s);
                        try {
                            Log.e("RSenL", "thread id:" + Thread.currentThread().getId());
                            URL url = new URL("http://www.baidu.com");
                            URLConnection urlConnection = url.openConnection();
                            urlConnection.connect();
                            Thread.sleep(2000);
                            Log.e("RSenL", "call -- -----------------------------");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
        ;

        Observable.just("Hello")
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e("RSenL", "thread id:" + Thread.currentThread().getId());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("RSenL", "call -- " + s);
                    }
                });
    }

    private void rxDemo3() {
        Observable.just("Hello", "Hi", "Ok", "Rsen")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e("RSenL", "thread id:" + Thread.currentThread().getId());
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("RSenL", "thread id:" + Thread.currentThread().getId());
                    }
                });
    }

    private void rxDemo4() {
        Observable.just("a", "b", "c").subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e("0", "tid:" + Thread.currentThread().getId());

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                Log.e("1", "tid:" + Thread.currentThread().getId());

                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                Log.e("2", "tid:" + Thread.currentThread().getId());
                return null;
            }
        }).observeOn(Schedulers.newThread()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                Log.e("3", "tid:" + Thread.currentThread().getId());
                return null;
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                Log.e("4-filter", "tid:" + Thread.currentThread().getId());
                return true;
            }
        }).take(4)
                .observeOn(Schedulers.io()).map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                Log.e("4", "tid:" + Thread.currentThread().getId());
                return null;
            }
        }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("5", "tid:" + Thread.currentThread().getId());
            }
        });


    }

    private void rxDemo5() {
        Observable.just("t1", "t2", "t3").subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        try {
                            Log.e("1", "tid:" + Thread.currentThread().getId());
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .doOnEach(new Action1<Notification<? super String>>() {
                    @Override
                    public void call(Notification<? super String> notification) {
                        try {
                            Log.e("1+" + notification.getValue(), "tid:" + Thread.currentThread().getId());
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .doOnEach(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            Log.e("1-" + s, "tid:" + Thread.currentThread().getId());
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                try {
                    Log.e("2", "tid:" + Thread.currentThread().getId());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void rxDemo6() {
        Observable.just("Up Image").doOnSubscribe(new Action0() {
            @Override
            public void call() {
                try {
                    Log.e("1", "tid:" + Thread.currentThread().getId());//io线程
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            Log.e("2", "tid:" + Thread.currentThread().getId());//主线程
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
