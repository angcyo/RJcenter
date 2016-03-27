package com.angcyo.sample.DbDemo.db;


import com.rsen.db.DbException;
import com.rsen.db.DbManager;
import com.rsen.db.annotation.Column;
import com.rsen.db.annotation.Table;

import java.util.Date;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-7-25
 * Time: 下午7:06
 */
@Table(name = "parent", onCreated = "CREATE UNIQUE INDEX index_name ON parent(name,email)")
public class Parent {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    private String email;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Column(name = "time")
    private Date time;

    @Column(name = "date")
    private java.sql.Date date;

    public List<Child> getChildren(DbManager db) throws DbException {
        return db.selector(Child.class).where("parentId", "=", this.id).findAll();
    }

    // 一对一
    //public Child getChild(DbManager db) throws DbException {
    //    return db.selector(Child.class).where("parentId", "=", this.id).findFirst();
    //}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
