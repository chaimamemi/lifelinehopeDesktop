package models;

public class Bracelet {
    private int id;
    private Integer biologicalDataId; // Cet attribut est présent mais nous ne gérons pas l'objet BiologicalData directement.
    private Integer alertId; // Cet attribut est présent mais nous ne gérons pas l'objet Alert directement.
    private String identificationCode;
    private String temperature;
    private String bloodPressure;
    private Double heartRate;
    private String movement;
    private String gps;
    private Double latitude;
    private Double longitude;

    // Constructeur par défaut
    public Bracelet() {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getBiologicalDataId() {
        return biologicalDataId;
    }

    public void setBiologicalDataId(Integer biologicalDataId) {
        this.biologicalDataId = biologicalDataId;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = String.valueOf(temperature);
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Double heartRate) {
        this.heartRate = heartRate;
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

    // Méthode toString pour afficher les informations du bracelet
    @Override
    public String toString() {
        return "Bracelet{" +
                "id=" + id +
                ", biologicalDataId=" + biologicalDataId +
                ", alertId=" + alertId +
                ", identificationCode='" + identificationCode + '\'' +
                ", temperature=" + temperature +
                ", bloodPressure='" + bloodPressure + '\'' +
                ", heartRate=" + heartRate +
                ", movement='" + movement + '\'' +
                ", gps='" + gps + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}