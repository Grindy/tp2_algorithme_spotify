package com.maisonneuve.tp2_algorithme_spotify.model;

import java.sql.Timestamp;
import java.util.Date;

public class AuditJournalier {

    private int id;
    private String idChanson;
    private Date dateLecture;

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

    public Date getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(Timestamp dateLecture) {
        this.dateLecture = dateLecture;
    }
}
