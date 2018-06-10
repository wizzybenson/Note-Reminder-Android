package com.ernest.gestionnote;

import java.util.Date;

public class Note {
    private String description;
    private String color;
    private int priority;
    private Date echeance;

    public Note(String description, String color, int priority, Date echeance) {
        this.description = description;
        this.color = color;
        this.priority = priority;
        this.echeance = echeance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getEcheance() {
        return echeance;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }
}
