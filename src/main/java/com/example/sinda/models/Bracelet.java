package com.example.sinda.models;

public class Bracelet {
    private int id;
    private Integer biological_data_id; // Cet attribut est présent mais nous ne gérons pas l'objet BiologicalData directement.
    private String identification_code;
    private Double temperature;
    private Double blood_pressure;
    private Double heart_rate;
    private String movement;
    private String gps;
    private Double latitude;
    private Double longitude;
    private int iduser;

    public Bracelet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getBiological_data_id() {
        return biological_data_id;
    }

    public void setBiological_data_id(Integer biological_data_id) {
        this.biological_data_id = biological_data_id;
    }

    public String getIdentification_code() {
        return identification_code;
    }

    public void setIdentification_code(String identification_code) {
        this.identification_code = identification_code;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(Double blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public Double getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(Double heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    @Override
    public String toString() {
        return "Bracelet{" +
                "id=" + id +
                ", biological_data_id=" + biological_data_id +
                ", identification_code='" + identification_code + '\'' +
                ", temperature=" + temperature +
                ", blood_pressure='" + blood_pressure + '\'' +
                ", heart_rate=" + heart_rate +
                ", movement='" + movement + '\'' +
                ", gps='" + gps + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", iduser=" + iduser +
                '}';
    }
}
