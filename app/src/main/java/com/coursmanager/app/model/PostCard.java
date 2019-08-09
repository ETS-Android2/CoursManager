package com.coursmanager.app.model;

public class PostCard {

    private long idPostCard;
    private String theme;
    private String recto;
    private String verso;
    private long idLesson;

    public PostCard(long id, String t, String r, String v, long idL){
        this.idPostCard = id;
        this.theme = t;
        this.recto = r;
        this.verso = v;
        this.idLesson = idL;
    }

    public long getIdPostCard() {
        return idPostCard;
    }

    public void setIdPostCard(long idPostCard) {
        this.idPostCard = idPostCard;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getRecto() {
        return recto;
    }

    public void setRecto(String recto) {
        this.recto = recto;
    }

    public String getVerso() {
        return verso;
    }

    public void setVerso(String verso) {
        this.verso = verso;
    }

    public long getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(long idLesson) {
        this.idLesson = idLesson;
    }

}
