package com.example.coursmanager.model;

public class Lesson {

    private long idLesson;
    private String nameLesson;
    private String nameTeacher;
    private long date;
    private String note;
    private boolean finish;
    private long idSubject;

    public Lesson(long aId, String lesson, String teacher, long date, String note, boolean finish, long subject){
        this.idLesson = aId;
        this.nameLesson = lesson;
        this.nameTeacher = teacher;
        this.date = date;
        this.note = note;
        this.finish = finish;
        this.idSubject = subject;
    }

    public long getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(long idLesson) {
        this.idLesson = idLesson;
    }

    public String getNameLesson() {
        return nameLesson;
    }

    public void setNameLesson(String nameLesson) {
        this.nameLesson = nameLesson;
    }

    public String getNameTeacher() {
        return nameTeacher;
    }

    public void setNameTeacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public long getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(long idSubject) {
        this.idSubject = idSubject;
    }

}
