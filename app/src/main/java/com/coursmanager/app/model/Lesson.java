package com.coursmanager.app.model;

public class Lesson {

    private long idLesson;
    private String nameLesson;
    private String nameTeacher;
    private String dateJ0;
    private String note;
    private boolean finish;
    private long idSubject;
    private int objective;
    private int nbRead;
    private int rhythm;
    private int firstRead;
    private String dateMax;
    private boolean jMethod;
    private String nextRead;

    public Lesson(long aId, String lesson, String teacher, String dateJ0, String note, boolean finish, long subject, int objective, int nbRead, int rhythm, int firstRead, String dateMax, boolean jMethod, String nextRead){
        this.idLesson = aId;
        this.nameLesson = lesson;
        this.nameTeacher = teacher;
        this.dateJ0 = dateJ0;
        this.note = note;
        this.finish = finish;
        this.idSubject = subject;
        this.objective = objective;
        this.nbRead = nbRead;
        this.rhythm = rhythm;
        this.firstRead = firstRead;
        this.dateMax = dateMax;
        this.jMethod = jMethod;
        this.nextRead = nextRead;
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

    public String getDateJ0() {
        return dateJ0;
    }

    public void setDateJ0(String date) {
        this.dateJ0 = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isFinish() {
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

    public int getObjective() {
        return objective;
    }

    public void setObjective(int objective) {
        this.objective = objective;
    }

    public int getNbRead() {
        return nbRead;
    }

    public void setNbRead(int nbRead) {
        this.nbRead = nbRead;
    }

    public int getRhythm() {
        return rhythm;
    }

    public void setRhythm(int rhythm) {
        this.rhythm = rhythm;
    }

    public int getFirstRead() {
        return firstRead;
    }

    public void setFirstRead(int firstRead) {
        this.firstRead = firstRead;
    }

    public String getDateMax() {
        return dateMax;
    }

    public void setDateMax(String dateMax) {
        this.dateMax = dateMax;
    }

    public boolean isjMethod() {
        return jMethod;
    }

    public void setjMethod(boolean jMethod) {
        this.jMethod = jMethod;
    }

    public String getNextRead() {
        return nextRead;
    }

    public void setNextRead(String nextRead) {
        this.nextRead = nextRead;
    }
}
