package com.example.coursmanager.model;

// UE is Unité d'Enseignement in french -> Teaching Unit
public class UE {

    private long idUE;
    private String nameUE;
    private float percentageUE;

    public UE(long aId, String aName, float aPercentage){
        this.idUE = aId;
        this.nameUE = aName;
        this.percentageUE = aPercentage;
    }

    public long getIdUE() {
        return idUE;
    }

    public String getNameUE() {
        return nameUE;
    }

    public float getPercentageUE() {
        return percentageUE;
    }

    public void setIdUE(long idUE) {
        this.idUE = idUE;
    }

    public void setNameUE(String nameUE) {
        this.nameUE = nameUE;
    }

    public void setPercentageUE(float percentageUE) {
        this.percentageUE = percentageUE;
    }

}
