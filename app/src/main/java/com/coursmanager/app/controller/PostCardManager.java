package com.coursmanager.app.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.coursmanager.app.model.PostCard;

public class PostCardManager extends Manager {

    protected static final String TABLE_NAME_POSTCARD = "postcard";
    public static final String KEY_ID_POSTCARD = "_id";
    public static final String KEY_THEME_POSTCAR = "theme_postcard";
    public static final String KEY_RECTO_POSTCARD = "recto_postcard";
    public static final String KEY_VERSO_POSTCARD = "verso_postcard";
    public static final String KEY_IDLESSON_POSTCARD = "id_lesson";

    public static final String CREATE_TABLE_POSTCARD = "CREATE TABLE "+TABLE_NAME_POSTCARD+
            "("+KEY_ID_POSTCARD+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " "+KEY_THEME_POSTCAR+" TEXT,"+
            " "+KEY_RECTO_POSTCARD+" TEXT,"+
            " "+KEY_VERSO_POSTCARD+" TEXT,"+
            " "+KEY_IDLESSON_POSTCARD+" INTEGER,"+
            " FOREIGN KEY("+KEY_IDLESSON_POSTCARD+") REFERENCES "+LessonManager.TABLE_NAME_LESSON+"("+LessonManager.KEY_ID_LESSON+")"+" ON DELETE CASCADE"+
            ");";
    public PostCardManager(Context context){
        super(context);
    }

    public long addPostCard(PostCard aPostCard){
        ContentValues values = new ContentValues();
        values.put(KEY_THEME_POSTCAR, aPostCard.getTheme());
        values.put(KEY_RECTO_POSTCARD, aPostCard.getRecto());
        values.put(KEY_VERSO_POSTCARD, aPostCard.getVerso());
        values.put(KEY_IDLESSON_POSTCARD, aPostCard.getIdLesson());

        return db.insert(TABLE_NAME_POSTCARD, null, values);
    }

    public int updatePostCard(PostCard aPostCard){
        ContentValues values = new ContentValues();
        values.put(KEY_THEME_POSTCAR, aPostCard.getTheme());
        values.put(KEY_RECTO_POSTCARD, aPostCard.getRecto());
        values.put(KEY_VERSO_POSTCARD, aPostCard.getVerso());
        values.put(KEY_IDLESSON_POSTCARD, aPostCard.getIdLesson());

        String where = KEY_ID_POSTCARD+" = ?";
        String[] whereArgs = {aPostCard.getIdPostCard()+""};

        return db.update(TABLE_NAME_POSTCARD, values, where, whereArgs);
    }

    public void deletePostCard(PostCard aPostCard){
        db.execSQL("DELETE FROM "+TABLE_NAME_POSTCARD+" WHERE "+KEY_ID_POSTCARD+"="+aPostCard.getIdPostCard());
    }

    public PostCard getPostCard(long aId){
        PostCard p = new PostCard(0, "", "", "", 0);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_POSTCARD+" WHERE "+KEY_ID_POSTCARD+"="+aId, null);
        if(c.moveToFirst()){
            p.setIdPostCard(c.getInt(c.getColumnIndex(KEY_ID_POSTCARD)));
            p.setTheme(c.getString(c.getColumnIndex(KEY_THEME_POSTCAR)));
            p.setRecto(c.getString(c.getColumnIndex(KEY_RECTO_POSTCARD)));
            p.setVerso(c.getString(c.getColumnIndex(KEY_VERSO_POSTCARD)));
            p.setIdLesson(c.getInt(c.getColumnIndex(KEY_IDLESSON_POSTCARD)));
        }

        return p;
    }

    public Cursor getAllPostCardLesson(long idLesson){
        return db.rawQuery("SELECT * FROM "+TABLE_NAME_POSTCARD+" WHERE "+KEY_IDLESSON_POSTCARD+" = "+idLesson, null);
    }

}
