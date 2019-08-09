package com.coursmanager.app.model;

public class Subject {

    private long idSubject;
    private String nameSubject;
    private long idUE;
    private boolean finish;

    public Subject(long aId, String aName, long idUE, boolean f){
        this.idSubject = aId;
        this.nameSubject = aName;
        this.idUE = idUE;
        this.finish = f;
    }

    public void setIdSubject(long idSubject) {
        this.idSubject = idSubject;
    }

    public void setNameSubject(String nameSubject) {
        this.nameSubject = nameSubject;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void setIdUE(long idUE) {
        this.idUE = idUE;
    }

    public long getIdUE() {
        return idUE;
    }

    public long getIdSubject() {
        return idSubject;
    }

    public String getNameSubject() {
        return nameSubject;
    }

    public boolean getFinish() {
        return finish;
    }

}
