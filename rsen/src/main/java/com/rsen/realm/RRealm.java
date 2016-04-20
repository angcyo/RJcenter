package com.rsen.realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by robi on 2016-04-20 20:30.
 */
public class RRealm {
    public static void init(Context context, String name) {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(context).name(name).build());
    }

    public static Realm realm() {
        return Realm.getDefaultInstance();
    }
}
