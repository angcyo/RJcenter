package com.rsen.realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by robi on 2016-04-20 20:30.
 */
public class RRealm {
    /**
     * 必须调用的方法
     */
    public static void init(Context context, String name, boolean clean) {
        RealmConfiguration configuration = new RealmConfiguration.Builder(context).name(name).build();
        if (clean) {
            Realm.deleteRealm(configuration);
        }
        Realm.setDefaultConfiguration(configuration);
    }

    public static Realm realm() {
        return Realm.getDefaultInstance();
    }

    /**
     * 通常可以直接此方法
     */
    public static void operate(OnOperate operate) {
        if (operate != null) {
            Realm realm = realm();
            realm.beginTransaction();
            operate.on(realm);
            realm.commitTransaction();
            realm.close();
        }
    }

    public static void tran(final OnOperate operate) {
        if (operate != null) {
            realm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    operate.on(realm);
                }
            });
        }
    }

    public static void tranAsync(final OnOperate operate) {
        if (operate != null) {
            realm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    operate.on(realm);
                }
            });
        }
    }

    public static void tranAsync(final OnOperate operate, final OnError onError) {
        if (operate != null) {
            realm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    operate.on(realm);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    onError.onError(error);
                }
            });
        }
    }

    public static void tranAsync(final OnOperate operate, final OnSuccess onSuccess) {
        if (operate != null) {
            realm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    operate.on(realm);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    onSuccess.onSuccess();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                }
            });
        }
    }

    public static void tranAsync(final OnOperate operate, final OnSuccess onSuccess, final OnError onError) {
        if (operate != null) {
            realm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    operate.on(realm);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    onSuccess.onSuccess();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    onError.onError(error);
                }
            });
        }
    }

    public interface OnOperate {
        void on(Realm realm);
    }

    public interface OnSuccess {
        void onSuccess();
    }

    public interface OnError {
        void onError(Throwable error);
    }

}
