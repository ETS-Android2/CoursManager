package com.coursmanager.app.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.coursmanager.app.model.Subject;

public class SubjectManager extends Manager {

    protected static final String TABLE_NAME_SUBJECT = "subject";
    public static final String KEY_ID_SUBJECT = "_id";
    public static final String KEY_NAME_SUBJECT = "name_subject";
    public static final String KEY_IDUE_SUBJECT = "id_ue";
    public static final String KEY_FINISH_SUBJECT = "finish";

    public static final String CREATE_TABLE_SUBJECT = "CREATE TABLE "+TABLE_NAME_SUBJECT+
            "("+KEY_ID_SUBJECT+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " "+KEY_NAME_SUBJECT+" TEXT,"+
            " "+KEY_IDUE_SUBJECT+" INTEGER,"+
            " "+KEY_FINISH_SUBJECT+" NUMERIC,"+
            " FOREIGN KEY("+KEY_IDUE_SUBJECT+") REFERENCES "+UEManager.TABLE_NAME_UE+"("+UEManager.KEY_ID_UE+")"+" ON DELETE CASCADE"+
            ");";

    public SubjectManager(Context context){
        super(context);
    }

    // Return the id of new insert
    public long addSubject(Subject aSubject){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_SUBJECT, aSubject.getNameSubject());
        values.put(KEY_IDUE_SUBJECT, aSubject.getIdUE());
        values.put(KEY_FINISH_SUBJECT, aSubject.getFinish());

        return db.insert(TABLE_NAME_SUBJECT, null, values);
    }

    public int updateSubject(Subject aSubject){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_SUBJECT, aSubject.getNameSubject());
        values.put(KEY_IDUE_SUBJECT, aSubject.getIdUE());
        values.put(KEY_FINISH_SUBJECT, aSubject.getFinish());

        String where = KEY_ID_SUBJECT+" = ?";
        String[] whereArgs = {aSubject.getIdSubject()+""};

        return db.update(TABLE_NAME_SUBJECT, values, where, whereArgs);
    }

    public void deleteSubject(Subject aSubject){
        db.execSQL("DELETE FROM "+TABLE_NAME_SUBJECT+" WHERE "+KEY_ID_SUBJECT+"="+aSubject.getIdSubject());
    }

    public Subject getSubject(long aId){
        Subject sb = new Subject(0, "", 0, false);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_SUBJECT+" WHERE "+KEY_ID_SUBJECT+"="+aId, null);
        if(c.moveToFirst()){
            sb.setIdSubject(c.getInt(c.getColumnIndex(KEY_ID_SUBJECT)));
            sb.setNameSubject(c.getString(c.getColumnIndex(KEY_NAME_SUBJECT)));
            sb.setIdUE(c.getInt(c.getColumnIndex(KEY_IDUE_SUBJECT)));
            sb.setFinish(c.getInt(c.getColumnIndex(KEY_FINISH_SUBJECT)) > 0);
        }

        return sb;
    }

    public int getProgressOfaSubject(long aId){
        int nbTot = db.rawQuery("SELECT * FROM "+LessonManager.TABLE_NAME_LESSON+" WHERE "+LessonManager.KEY_IDSUBJECT_LESSON+" = "+aId, null).getCount();
        int nbFinished = db.rawQuery("SELECT * FROM "+LessonManager.TABLE_NAME_LESSON+" WHERE "+LessonManager.KEY_FINISH_LESSON+" = "+1+" AND "+LessonManager.KEY_IDSUBJECT_LESSON+" = "+aId, null).getCount();

        return (int) Math.round(((float)nbFinished/(float)nbTot)*100);
    }

    public Cursor getAllSubjectUE(long idUE, int order){
        String request = "SELECT * FROM "+TABLE_NAME_SUBJECT+" WHERE "+KEY_IDUE_SUBJECT+" = "+idUE;
        //Order 1 is by name ascendant, 2 by name descendant, 3 by creation date (default)
        switch (order){
            case 1:
                return db.rawQuery(request, null);
            case 2:
                return db.rawQuery(request+" ORDER BY "+KEY_NAME_SUBJECT, null);
            case 3:
                return db.rawQuery(request+" ORDER BY "+KEY_NAME_SUBJECT+" DESC ", null);
            default:
                return db.rawQuery(request, null);
        }
    }

    public void deleteAllSubjectUE(long idUE){
        db.execSQL("DELETE FROM "+TABLE_NAME_SUBJECT+" WHERE "+KEY_IDUE_SUBJECT+" = "+idUE);
    }

    @Override
    public void rename(String newName, long aId){
        ContentValues value = new ContentValues();
        value.put(KEY_NAME_SUBJECT, newName);
        db.update(TABLE_NAME_SUBJECT, value, KEY_ID_SUBJECT + "= ?", new String[] {aId+""});
    }

}
