package com.angcyo.sample.DbDemo;

import android.os.Bundle;

import com.angcyo.sample.DbDemo.db.Child;
import com.angcyo.sample.DbDemo.db.Parent;
import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.db.DbException;
import com.rsen.db.DbManager;
import com.rsen.db.X;
import com.rsen.db.table.DbModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DbActivity extends RBaseActivity {

    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("rsen_db_test.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });

    @Override
    protected int getContentView() {
        return R.layout.activity_db;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        test1();
        test2();
    }

    private void test1() {

        // 一对多: (本示例的代码)
        // 自己在多的一方(child)保存另一方的(parentId), 查找的时候用parentId查parent或child.
        // 一对一:
        // 在任何一边保存另一边的Id并加上唯一属性: @Column(name = "parentId", property = "UNIQUE")
        // 多对多:
        // 再建一个关联表, 保存两边的id. 查询分两步: 先查关联表得到id, 再查对应表的属性.

        String temp = "";

        try {
            DbManager db = X.getDb(daoConfig);

            Child child = new Child();
            child.setName("child's name");

            Parent test = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).findFirst();
            // long count = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).count();
            // Parent test = db.selector(Parent.class).where("id", "between", new String[]{"1", "5"}).findFirst();
            if (test != null) {
                child.setParentId(test.getId());
                temp += "first parent:" + test + "\n";
            }

            Parent parent = new Parent();
            parent.name = "测试" + System.currentTimeMillis();
            parent.setAdmin(true);
            parent.setEmail("wyouflf@qq.com");
            parent.setTime(new Date());
            parent.setDate(new java.sql.Date(new Date().getTime()));
            db.save(parent);

            db.saveBindingId(child);//保存对象关联数据库生成的id

            List<Child> children = db.selector(Child.class).findAll();
            temp += "children size:" + children.size() + "\n";
            if (children.size() > 0) {
                temp += "last children:" + children.get(children.size() - 1) + "\n";
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            calendar.add(Calendar.HOUR, 3);

            List<Parent> list = db.selector(Parent.class)
                    .where("id", "<", 54)
                    .and("time", ">", calendar.getTime())
                    .orderBy("id")
                    .limit(10).findAll();
            temp += "find parent size:" + list.size() + "\n";
            if (list.size() > 0) {
                temp += "last parent:" + list.get(list.size() - 1) + "\n";
            }

            //parent.name = "hahaha123";
            //db.update(parent);

            Parent entity = child.getParent(db);
            temp += "find by id:" + entity.toString() + "\n";

            List<DbModel> dbModels = db.selector(Parent.class)
                    .groupBy("name")
                    .select("name", "count(name) as count").findAll();
            temp += "group by result:" + dbModels.get(0).getDataMap() + "\n";

        } catch (Throwable e) {
            temp += "error :" + e.getMessage() + "\n";
        }
    }


    private void test2() {

        DbManager db = X.getDb(daoConfig);
        String result = "";

        List<Parent> parentList = new ArrayList<Parent>();
        for (int i = 0; i < 1000; i++) {
            Parent parent = new Parent();
            parent.setAdmin(true);
            parent.setDate(new java.sql.Date(1234));
            parent.setTime(new Date());
            parent.setEmail(i + "_@qq.com");
            parentList.add(parent);
        }

        long start = System.currentTimeMillis();
        for (Parent parent : parentList) {
            try {
                db.save(parent);
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
        result += "插入1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

        start = System.currentTimeMillis();
        try {
            parentList = db.selector(Parent.class).orderBy("id", true).limit(1000).findAll();
        } catch (DbException ex) {
            ex.printStackTrace();
        }
        result += "查找1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

        start = System.currentTimeMillis();
        try {
            db.delete(parentList);
        } catch (DbException ex) {
            ex.printStackTrace();
        }
        result += "删除1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

        // 批量插入
        parentList = new ArrayList<Parent>();
        for (int i = 0; i < 1000; i++) {
            Parent parent = new Parent();
            parent.setAdmin(true);
            parent.setDate(new java.sql.Date(1234));
            parent.setTime(new Date());
            parent.setEmail(i + "_@qq.com");
            parentList.add(parent);
        }

        start = System.currentTimeMillis();
        try {
            db.save(parentList);
        } catch (DbException ex) {
            ex.printStackTrace();
        }
        result += "批量插入1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

        try {
            parentList = db.selector(Parent.class).orderBy("id", true).limit(1000).findAll();
            db.delete(parentList);
        } catch (DbException ex) {
            ex.printStackTrace();
        }

        final String finalResult = result;
    }
}
