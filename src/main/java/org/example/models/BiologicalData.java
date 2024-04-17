package org.example.models;

public class BiologicalData {
    private int id;
    private String measurementType;
    private String value;
    private String patientName;
    private String patientLastName;
    private int patientAge;
    private String disease;
    private String otherInformation;

    public BiologicalData(int id, String measurementType, String value, String patientName, String patientLastName, int patientAge, String disease, String other_information) {
        this.id = id;
        this.measurementType = measurementType;
        this.value = value;
        this.patientName = patientName;
        this.patientLastName = patientLastName;
        this.patientAge = patientAge;
        this.disease = disease;
        this.otherInformation = other_information;
    }

    public BiologicalData() {
    }

    public BiologicalData(int id, String measurementType, String value, String patientName, String patientLastName, String patientAge, String disease) {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeasurementType() {
        return this.measurementType;
    }

    public void setMeasurementType(String measurementType) {
        if (measurementType != null && !measurementType.isEmpty()) {
            this.measurementType = measurementType;
        } else {
            throw new IllegalArgumentException("Measurement type cannot be null or empty");
        }
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        if (value != null && !value.isEmpty()) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
    }

    public String getPatientName() {
        return this.patientName;
    }

    public void setPatientName(String patientName) {
        if (patientName != null && !patientName.isEmpty()) {
            this.patientName = patientName;
        } else {
            throw new IllegalArgumentException("Patient name cannot be null or empty");
        }
    }

    public String getPatientLastName() {
        return this.patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        if (patientLastName != null && !patientLastName.isEmpty()) {
            this.patientLastName = patientLastName;
        } else {
            throw new IllegalArgumentException("Patient last name cannot be null or empty");
        }
    }

    public int getPatientAge() {
        return this.patientAge;
    }

    public void setPatientAge(int patientAge) {
        if (patientAge >= 0) {
            this.patientAge = patientAge;
        } else {
            throw new IllegalArgumentException("Patient age cannot be negative");
        }
    }

    public String getDisease() {
        return this.disease;
    }

    public void setDisease(String disease) {
        if (disease != null && !disease.isEmpty()) {
            this.disease = disease;
        } else {
            throw new IllegalArgumentException("Disease cannot be null or empty");
        }
    }

    public String getOtherInformation() {
        return this.otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public String toString() {
        return "BiologicalData{id=" + this.id + ", measurementType='" + this.measurementType + "', value='" + this.value + "', patientName='" + this.patientName + "', patientLastName='" + this.patientLastName + "', patientAge=" + this.patientAge + ", disease='" + this.disease + "', otherInformation='" + this.otherInformation + "'}";
    }
}

