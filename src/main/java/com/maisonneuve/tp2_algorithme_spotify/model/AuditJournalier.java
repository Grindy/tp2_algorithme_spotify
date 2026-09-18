package com.maisonneuve.tp2_algorithme_spotify.model;

import java.sql.Timestamp;
import java.util.Date;

public class AuditJournalier {

    private int id;
    private String idChanson;
    private Timestamp dateLecture;
    private String titre;

    public AuditJournalier(String idChanson) {
        this.idChanson = idChanson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdChanson() {
        return idChanson;
    }

    public void setIdChanson(String idChanson) {
        this.idChanson = idChanson;
    }

    public Timestamp getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(Timestamp dateLecture) {
        this.dateLecture = dateLecture;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDateLectureFormatee() {
        return (dateLecture != null) ? dateLecture.toString().substring(0, 19) : "";
    }
}
