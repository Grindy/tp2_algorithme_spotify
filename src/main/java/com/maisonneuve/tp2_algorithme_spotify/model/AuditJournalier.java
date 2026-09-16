package com.maisonneuve.tp2_algorithme_spotify.model;

import java.util.Date;

public class AuditJournalier {

    private String id;
    private String idChanson;
    private Date dateJouee;

    public AuditJournalier(String idChanson) {
        this.idChanson = idChanson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdChanson() {
        return idChanson;
    }

    public void setIdChanson(String idChanson) {
        this.idChanson = idChanson;
    }

    public Date getDateJouee() {
        return dateJouee;
    }

    public void setDateJouee(Date dateJouee) {
        this.dateJouee = dateJouee;
    }
}
