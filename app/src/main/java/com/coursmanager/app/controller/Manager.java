package com.coursmanager.app.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.coursmanager.app.tools.MySQLite;

public class Manager {

    protected MySQLite mySQLiteBase;
    protected SQLiteDatabase db;

    public Manager(Context context) {
        mySQLiteBase = MySQLite.getInstance(context);
    }

    public void open(){
        db = mySQLiteBase.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void rename(String newName, long aId){};
}
