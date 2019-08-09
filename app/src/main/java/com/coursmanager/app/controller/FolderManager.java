package com.coursmanager.app.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.coursmanager.app.model.Folder;

public class FolderManager extends Manager {

    protected static final String TABLE_NAME_FOLDER = "folder";
    public static final String KEY_ID_FOLDER = "_id";
    public static final String KEY_NAME_FOLDER = "name_folder";
    public static final String KEY_PERCENTAGE_FOLDER = "percentage_folder";
    public static final String CREATE_TABLE_FOLDER = "CREATE TABLE "+TABLE_NAME_FOLDER+
            "("+KEY_ID_FOLDER+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " "+KEY_NAME_FOLDER+" TEXT,"+
            " "+KEY_PERCENTAGE_FOLDER+" REAL"+
            ");";

    public FolderManager(Context context){
        super(context);
    }

    public long addFolder(Folder aFolder){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_FOLDER, aFolder.getNameFolder());
        values.put(KEY_PERCENTAGE_FOLDER, aFolder.getPercentFolder());
        
        return db.insert(TABLE_NAME_FOLDER, null, values);
    }

    public int updateFolder(Folder aFolder){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_FOLDER, aFolder.getNameFolder());
        values.put(KEY_PERCENTAGE_FOLDER, aFolder.getPercentFolder());

        String where = KEY_ID_FOLDER+" = ?";
        String[] whereArgs = {aFolder.getIdFolder()+""};

        return db.update(TABLE_NAME_FOLDER, values, where, whereArgs);
    }

    public void deleteFolder(Folder aFolder){
        db.execSQL("DELETE FROM "+TABLE_NAME_FOLDER+" WHERE "+KEY_ID_FOLDER+"="+aFolder.getIdFolder());
    }

    public Folder getFolder(long aId){
        Folder folder = new Folder(0, "", 0);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_FOLDER+" WHERE "+KEY_ID_FOLDER+"="+aId, null);
        if(c.moveToFirst()){
            folder.setIdFolder(c.getInt(c.getColumnIndex(KEY_ID_FOLDER)));
            folder.setNameFolder(c.getString(c.getColumnIndex(KEY_NAME_FOLDER)));
            folder.setPercentFolder(c.getInt(c.getColumnIndex(KEY_PERCENTAGE_FOLDER)));
        }

        return folder;
    }

    public Cursor getAllFolder(int order){
        String request = "SELECT * FROM "+TABLE_NAME_FOLDER;
        //Order 1 is by name ascendant, 2 by name descendant, 3 by creation date (default)
        switch (order){
            case 1:
                return db.rawQuery(request, null);
            case 2:
                return db.rawQuery(request+" ORDER BY "+KEY_NAME_FOLDER, null);
            case 3:
                return db.rawQuery(request+" ORDER BY "+KEY_NAME_FOLDER+" DESC ", null);
            default:
                return db.rawQuery(request, null);
        }
    }

    public void deleteAllFolder(){
        db.execSQL("DELETE FROM "+TABLE_NAME_FOLDER);
    }

    @Override
    public void rename(String newName, long aId){
        ContentValues value = new ContentValues();
        value.put(KEY_NAME_FOLDER, newName);
        db.update(TABLE_NAME_FOLDER, value, KEY_ID_FOLDER + "= ?", new String[] {aId+""});
    }

}
