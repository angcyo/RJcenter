package com.angcyo.sample.realm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by robi on 2016-04-20 20:40.
 */
@RealmClass
public class TestRealmObject extends RealmObject {

    @Required
    public String name;
    public int age;
    public long time;

    @Ignore
    public String test;

    public TestRealmObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name-->").append(name).append("\n");
        stringBuilder.append("age -->").append(age).append("\n");
        stringBuilder.append("time-->").append(time).append("\n");
        stringBuilder.append("test-->").append(test);
        return stringBuilder.toString();
    }
}
