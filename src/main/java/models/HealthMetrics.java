package models;

public class HealthMetrics {
    private double averageTemperature;
    private double averageHeartRate;
    private double averageBloodPressure;
    private int totalCount; // Total count of bracelets analyzed
    private double totalTemperature;
    private double totalHeartRate;
    private double totalBloodPressure;
    private String healthAdvice;

    public HealthMetrics(double averageTemperature, double averageHeartRate, double averageBloodPressure, int totalCount, String healthAdvice, double totalTemperature, double totalHeartRate, double totalBloodPressure) {
        this.averageTemperature = averageTemperature;
        this.averageHeartRate = averageHeartRate;
        this.averageBloodPressure = averageBloodPressure;
        this.totalCount = totalCount;
        this.healthAdvice = healthAdvice;
        this.totalTemperature = totalTemperature;
        this.totalHeartRate = totalHeartRate;
        this.totalBloodPressure = totalBloodPressure;
    }
    // Simplified constructor
    public HealthMetrics(double averageTemperature, double averageHeartRate, double averageBloodPressure, int totalCount, String healthAdvice) {
        this.averageTemperature = averageTemperature;
        this.averageHeartRate = averageHeartRate;
        this.averageBloodPressure = averageBloodPressure;
        this.totalCount = totalCount;
        this.healthAdvice = healthAdvice;
        // Initialize total values to the average values for simplicity
        this.totalTemperature = averageTemperature;
        this.totalHeartRate = averageHeartRate;
        this.totalBloodPressure = averageBloodPressure;
    }


    public double getAverageTemperature() {
        return averageTemperature;
    }

    public double getAverageHeartRate() {
        return averageHeartRate;
    }

    public double getAverageBloodPressure() {
        return averageBloodPressure;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getHealthAdvice() {
        return healthAdvice;
    }

    public double getTemperaturePercentage() {
        return totalTemperature > 0 ? (averageTemperature / totalTemperature) * 100 : 0;
    }

    public double getHeartRatePercentage() {
        return totalHeartRate > 0 ? (averageHeartRate / totalHeartRate) * 100 : 0;
    }

    public double getBloodPressurePercentage() {
        return totalBloodPressure > 0 ? (averageBloodPressure / totalBloodPressure) * 100 : 0;
    }
}
