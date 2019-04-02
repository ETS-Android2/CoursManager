package com.example.coursmanager.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursmanager.R;
import com.example.coursmanager.model.UE;
import com.example.coursmanager.tools.MySQLite;

public class UEManager {

    protected static final String TABLE_NAME = "ue";
    public static final String KEY_ID_UE = "_id";
    public static final String KEY_NAME_UE = "name_ue";
    public static final String KEY_PERCENTAGE_UE = "percentage_ue";
    public static final String CREATE_TABLE_UE = "CREATE TABLE "+TABLE_NAME+
            "("+KEY_ID_UE+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " "+KEY_NAME_UE+" TEXT,"+
            " "+KEY_PERCENTAGE_UE+" REAL"+
            ");";
    //CREATE TABLE t1(a INT, b TEXT, c REAL);
    private MySQLite mySQLiteBase;
    private SQLiteDatabase db;

    public UEManager(Context context){
        mySQLiteBase = MySQLite.getInstance(context);
    }

    public void open(){
        db = mySQLiteBase.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    // Return the id of new insert
    public long addUE(UE aUE){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_UE, aUE.getNameUE());
        values.put(KEY_PERCENTAGE_UE, aUE.getPercentageUE());

        return db.insert(TABLE_NAME, null, values);
    }

    public int updateUE(UE aUE){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_UE, aUE.getNameUE());
        values.put(KEY_PERCENTAGE_UE, aUE.getPercentageUE());

        String where = KEY_ID_UE+" = ?";
        String[] whereArgs = {aUE.getIdUE()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public void deleteUE(UE aUE){
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+KEY_ID_UE+"="+aUE.getIdUE());
    }

    public UE getUE(long aId){
        UE ue = new UE(0, "", 0);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_UE+"="+aId, null);
        if(c.moveToFirst()){
            ue.setIdUE(c.getInt(c.getColumnIndex(KEY_ID_UE)));
            ue.setNameUE(c.getString(c.getColumnIndex(KEY_NAME_UE)));
            ue.setPercentageUE(c.getFloat(c.getColumnIndex(KEY_PERCENTAGE_UE)));
        }

        return ue;
    }

    public Cursor getAllUE(){
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

}
