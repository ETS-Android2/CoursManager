package com.coursmanager.app.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coursmanager.app.controller.FolderManager;
import com.coursmanager.app.controller.LessonManager;
import com.coursmanager.app.controller.PostCardManager;
import com.coursmanager.app.controller.SubjectManager;
import com.coursmanager.app.controller.UEManager;

public class MySQLite extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "myData.db";
    private static final int DATABASE_VERSION = 6;
    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context){
        if (sInstance == null) { sInstance = new MySQLite(context); }
        return sInstance;
    }

    private MySQLite(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        // Create Data Base
        sqLiteDatabase.execSQL(FolderManager.CREATE_TABLE_FOLDER);
        sqLiteDatabase.execSQL(UEManager.CREATE_TABLE_UE);
        sqLiteDatabase.execSQL(SubjectManager.CREATE_TABLE_SUBJECT);
        sqLiteDatabase.execSQL(LessonManager.CREATE_TABLE_LESSON);
        sqLiteDatabase.execSQL(PostCardManager.CREATE_TABLE_POSTCARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2){
        // Update Data Base
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

}