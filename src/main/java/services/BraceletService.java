package services;

import connectionDB.DatabaseConnector;
import models.Bracelet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import models.HealthMetrics;
import java.util.List;


public class BraceletService {
    private final Connection cnx;

    public BraceletService() {
        this.cnx = DatabaseConnector.getInstance().getCnx();
    }

    public List<Bracelet> getAllBracelets() {
        List<Bracelet> bracelets = new ArrayList<>();
        String sql = "SELECT * FROM bracelet";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Bracelet bracelet = new Bracelet();
                bracelet.setId(rs.getInt("id"));
                bracelet.setBiologicalDataId((Integer) rs.getObject("biological_data_id"));

                bracelet.setIdentificationCode(rs.getString("identification_code"));
                bracelet.setBloodPressure(rs.getString("blood_pressure"));
                bracelet.setMovement(rs.getString("movement"));
                bracelet.setGps(rs.getString("gps"));

                String tempStr = rs.getString("temperature");
                if (tempStr != null && !tempStr.isEmpty()) {
                    try {
                        bracelet.setTemperature(Double.valueOf(tempStr.replace("°", "")));
                    } catch (NumberFormatException e) {
                        bracelet.setTemperature(null);
                        // Log or handle the format error
                    }
                }

                try {
                    Double heartRate = rs.getDouble("heart_rate");
                    if (!rs.wasNull()) {
                        bracelet.setHeartRate(heartRate);
                    }
                } catch (SQLException e) {
                    bracelet.setHeartRate(null); // handle the error
                }

                try {
                    Double latitude = rs.getDouble("latitude");
                    if (!rs.wasNull()) {
                        bracelet.setLatitude(latitude);
                    }
                } catch (SQLException e) {
                    bracelet.setLatitude(null); // handle the error
                }

                try {
                    Double longitude = rs.getDouble("longitude");
                    if (!rs.wasNull()) {
                        bracelet.setLongitude(longitude);
                    }
                } catch (SQLException e) {
                    bracelet.setLongitude(null); // handle the error
                }

                bracelets.add(bracelet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bracelets;
    }

    public List<Bracelet> getAllBraceletss() {
        List<Bracelet> bracelets = new ArrayList<>();
        String sql = "SELECT * FROM bracelet";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Bracelet bracelet = new Bracelet();
                bracelet.setId(rs.getInt("id"));
                bracelet.setIdentificationCode(rs.getString("identification_code"));
                bracelet.setTemperature(rs.getDouble("temperature"));
                bracelet.setHeartRate(rs.getDouble("heart_rate"));
                bracelet.setBloodPressure(rs.getString("blood_pressure"));
                bracelets.add(bracelet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bracelets;
    }

    public Bracelet getLastBracelet() {
        Bracelet bracelet = null;
        String sql = "SELECT * FROM bracelet ORDER BY id DESC LIMIT 1";  // Sélectionne la dernière ligne
        try (PreparedStatement pstmt = cnx.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                bracelet = new Bracelet();
                bracelet.setId(rs.getInt("id"));
                bracelet.setBiologicalDataId(rs.getObject("biological_data_id") != null ? rs.getInt("biological_data_id") : null);

                bracelet.setIdentificationCode(rs.getString("identification_code"));
                bracelet.setTemperature(rs.getObject("temperature") != null ? rs.getDouble("temperature") : null);
                bracelet.setBloodPressure(rs.getString("blood_pressure"));
                bracelet.setHeartRate(rs.getObject("heart_rate") != null ? rs.getDouble("heart_rate") : null);
                bracelet.setMovement(rs.getString("movement"));
                bracelet.setGps(rs.getString("gps"));
                // Assurez-vous de récupérer aussi la latitude et la longitude
                bracelet.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                bracelet.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bracelet;
    }


    public String generateBraceletAnalysis() {
        List<Bracelet> bracelets = getAllBracelets();
        StringBuilder analysis = new StringBuilder();

        // Bracelet data analysis
        double totalTemperature = 0.0;
        double totalHeartRate = 0.0;
        int totalBloodPressureHigh = 0;
        int totalBloodPressureNormal = 0;
        int totalBloodPressureLow = 0;
        int totalMovement = 0;

        for (Bracelet bracelet : bracelets) {
            String tempStr = bracelet.getTemperature() != null ? bracelet.getTemperature() : null;
            if (tempStr != null && !tempStr.isEmpty()) {
                tempStr = tempStr.replace("°", ""); // Remove the "°" character from the string
                try {
                    double temperatureValue = Double.parseDouble(tempStr);
                    totalTemperature += temperatureValue;
                } catch (NumberFormatException e) {
                    // Log or handle the format error
                }
            }

            totalHeartRate += bracelet.getHeartRate() != null ? bracelet.getHeartRate() : 0.0;
            totalMovement += bracelet.getMovement() != null ? 1 : 0;

            // Blood pressure analysis
            String bloodPressure = bracelet.getBloodPressure();
            if (bloodPressure != null && !bloodPressure.isEmpty()) {
                double bpValue = Double.parseDouble(bloodPressure.split("/")[0]); // Systolic blood pressure value
                if (bpValue >= 140) {
                    totalBloodPressureHigh++;
                } else if (bpValue >= 90) {
                    totalBloodPressureNormal++;
                } else {
                    totalBloodPressureLow++;
                }
            }
        }

        // Generate analysis report
        analysis.append("Bracelet Data Analysis:\n\n");
        analysis.append("Average Temperature: ").append(bracelets.isEmpty() ? 0.0 : totalTemperature / bracelets.size()).append(" °C\n");
        analysis.append("Average Heart Rate: ").append(bracelets.isEmpty() ? 0.0 : totalHeartRate / bracelets.size()).append(" bpm\n");
        analysis.append("Number of Recorded Movements: ").append(totalMovement).append("\n");
        analysis.append("High Blood Pressure (>= 140 mmHg): ").append(totalBloodPressureHigh).append(" cases\n");
        analysis.append("Normal Blood Pressure (90-139 mmHg): ").append(totalBloodPressureNormal).append(" cases\n");
        analysis.append("Low Blood Pressure (< 90 mmHg): ").append(totalBloodPressureLow).append(" cases\n");

        // Add advice based on analysis
        analysis.append("\nAdvice:\n");
        analysis.append("For cases of high blood pressure, consult a doctor.\n");
        analysis.append("Maintain regular physical activity for good movement.\n");

        return analysis.toString(); // Return the analysis report as a string
    }



    public HealthMetrics generateAdvancedHealthAnalysis() {
        List<Bracelet> bracelets = getAllBracelets();
        double totalTemperature = 0.0;
        double totalHeartRate = 0.0;
        double totalBloodPressure = 0.0;
        int count = 0;

        StringBuilder advice = new StringBuilder();
        Set<String> adviceSet = new HashSet<>(); // Use a set to track unique pieces of advice

        for (Bracelet bracelet : bracelets) {
            double temperature = tryParseDouble(bracelet.getTemperature());
            double heartRate = bracelet.getHeartRate() != null ? bracelet.getHeartRate() : 0.0;
            double bloodPressure = tryParseBloodPressure(bracelet.getBloodPressure());

            totalTemperature += temperature;
            totalHeartRate += heartRate;
            totalBloodPressure += bloodPressure;
            count++;

            // Append advice if not already included
            if (temperature >= 37.5 && !adviceSet.contains("fever")) {
                advice.append("High temperature could indicate fever, infection, or hyperthermia.\n");
                adviceSet.add("fever");
            }
            if (heartRate >= 100 && !adviceSet.contains("stress")) {
                advice.append("High heart rate could indicate stress, anxiety, or cardiovascular issues.\n");
                adviceSet.add("stress");
            }
            if (bloodPressure > 140 && !adviceSet.contains("hypertension")) {
                advice.append("High blood pressure may suggest hypertension.\n");
                adviceSet.add("hypertension");
            }
        }

        double averageTemperature = count > 0 ? totalTemperature / count : 0;
        double averageHeartRate = count > 0 ? totalHeartRate / count : 0;
        double averageBloodPressure = count > 0 ? totalBloodPressure / count : 0;

        // Returns new HealthMetrics object with computed averages and consolidated advice
        return new HealthMetrics(averageTemperature, averageHeartRate, averageBloodPressure, count, advice.toString());
    }

    private double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0; // Return a default value if parsing fails
        }
    }

    private double tryParseBloodPressure(String value) {
        try {
            if (value != null && value.contains("/")) {
                return Double.parseDouble(value.split("/")[0]);  // Only take the systolic part
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0; // Return a default value if parsing fails
        }
    }


}
