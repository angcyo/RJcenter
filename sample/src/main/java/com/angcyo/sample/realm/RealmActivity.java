package com.angcyo.sample.realm;

import android.os.Bundle;
import android.util.Log;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.realm.RRealm;
import com.rsen.util.DebugTime;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmActivity extends RBaseActivity {

    public static final int Num = 1000;

    public static void e(String log) {
        Log.e("angcyo", log);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_realm;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewHolder.v(R.id.add).setOnClickListener(v -> {
            add();
        });
        mViewHolder.v(R.id.delete).setOnClickListener(v -> {
            delete();
        });
        mViewHolder.v(R.id.update).setOnClickListener(v -> {
            update();
        });
        mViewHolder.v(R.id.query).setOnClickListener(v -> {
            query();
        });
        mViewHolder.v(R.id.add).setOnClickListener(v -> {
            add();
        });
        mViewHolder.v(R.id.delete2).setOnClickListener(v -> {
            delete2();
        });
        mViewHolder.v(R.id.update).setOnClickListener(v -> {
            update();
        });
        mViewHolder.v(R.id.query).setOnClickListener(v -> {
            query();
        });
    }

    private void add() {
        DebugTime.init();
        RRealm.operate(realm -> {
            for (int i = 0; i < Num; i++) {
                TestRealmObject realmObject = realm.createObject(TestRealmObject.class);
//                realmObject.setAge(i);
//                realmObject.setName("name" + i);
//                realmObject.setTest("test" + i);
//                realmObject.setTime(System.currentTimeMillis());
                realmObject.age = i;
                realmObject.name = "name" + i;
                realmObject.test = "test" + i;
                realmObject.time = System.currentTimeMillis();
            }
        });
        DebugTime.time();
    }

    private void delete() {
        DebugTime.init();
        RRealm.operate(realm -> {
            RealmResults<TestRealmObject> all = realm.where(TestRealmObject.class).findAll();
            all.clear();
        });
        DebugTime.time();
    }

    private void delete2() {
        DebugTime.init();
        RRealm.operate(realm -> {
            RealmResults<TestRealmObject> all = realm.where(TestRealmObject.class).findAll();
            for (int i = 0; i < all.size(); i++) {
                if (i % 4 == 0) {
                    all.get(i).removeFromRealm();
                }
            }
        });
        DebugTime.time();
    }

    private void update() {
        DebugTime.init();
        RRealm.operate(realm -> {
            RealmResults<TestRealmObject> all = realm.where(TestRealmObject.class).findAll();
            for (int i = 0; i < all.size(); i++) {
                TestRealmObject object = all.get(i);
//                object.setName(object.getName() + " new");
//                object.setTest(object.getTest() + " new");
            }
        });
        DebugTime.time();
    }

    private void query() {
        DebugTime.init();
        RRealm.operate(realm -> {
            RealmQuery<TestRealmObject> where = realm.where(TestRealmObject.class);
            RealmResults<TestRealmObject> all = where.findAll();
            for (TestRealmObject object : all) {
                e(object.toString());
            }
        });
        DebugTime.time();
    }
}
