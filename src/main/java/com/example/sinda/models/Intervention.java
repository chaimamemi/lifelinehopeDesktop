package com.example.sinda.models;

import java.time.LocalDateTime;

public class Intervention {
    private int id;
    private LocalDateTime dateTime;
    private String action;
    private String otherAction;
    private int patientId;
    private int emergencyTeamId;
    private int alertId;

    // Constructeur
    public Intervention() {
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOtherAction() {
        return otherAction;
    }

    public void setOtherAction(String otherAction) {
        this.otherAction = otherAction;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getEmergencyTeamId() {
        return emergencyTeamId;
    }

    public void setEmergencyTeamId(int emergencyTeamId) {
        this.emergencyTeamId = emergencyTeamId;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    // toString method
    @Override
    public String toString() {
        return "Intervention{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", action='" + action + '\'' +
                ", otherAction='" + otherAction + '\'' +
                ", patientId=" + patientId +
                ", emergencyTeamId=" + emergencyTeamId +
                ", alertId=" + alertId +
                '}';
    }
}