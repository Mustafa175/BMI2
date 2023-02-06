package com.example.bmi2;

import java.util.Date;

public class Measurement {
    private int id;
    private int personId;
    private double bmi;
    private Date date;

    public Measurement(int id, int personId, double bmi, Date date) {
        this.id = id;
        this.personId = personId;
        this.bmi = bmi;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
