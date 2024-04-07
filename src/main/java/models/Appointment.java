package models;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int patientId;
    private LocalDateTime dateTime;
    private String description;
    private String status;
    private int doctorId;

    // Constructeurs, getters et setters
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

    // toString method
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", doctorId=" + doctorId +
                '}';
    }
}
