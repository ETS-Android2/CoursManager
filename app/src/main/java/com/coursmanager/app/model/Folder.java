package com.coursmanager.app.model;

public class Folder {

    private long idFolder;
    private String nameFolder;
    private int percentFolder;

    public Folder(long aId, String aName, int aPercent){
        this.idFolder = aId;
        this.nameFolder = aName;
        this.percentFolder = aPercent;
    }

    public long getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(long idFolder) {
        this.idFolder = idFolder;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public int getPercentFolder() {
        return percentFolder;
    }

    public void setPercentFolder(int percentFolder) {
        this.percentFolder = percentFolder;
    }
}
