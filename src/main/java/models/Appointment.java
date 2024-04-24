package models;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int patientId;
    private LocalDateTime dateTime;
    private String description;
    private String status;
    private int doctorId;

    private boolean isConfirmed = false; // Valeur par défaut false
    private boolean isUrgent = false; // Valeur par défaut false

    // Constructeur par défaut
    public Appointment() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public int getDoctorId() {
        return doctorId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    // Setter pour isConfirmed
    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    // Getter pour isConfirmed
    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    // Setter pour isUrgent
    public void setIsUrgent(boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    // Getter pour isUrgent
    public boolean getIsUrgent() {
        return isUrgent;
    }

    // toString method
    @Override
    public String toString() {
        // Mettre à jour le toString pour inclure les nouveaux champs
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", doctorId=" + doctorId +
                ", isConfirmed=" + isConfirmed +
                ", isUrgent=" + isUrgent +
                '}';
    }
}
