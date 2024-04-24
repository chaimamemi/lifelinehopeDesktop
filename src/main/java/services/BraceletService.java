package services;

import connectionDB.DatabaseConnector;
import models.Bracelet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                bracelet.setAlertId((Integer) rs.getObject("alert_id"));
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

    public Bracelet getLastBracelet() {
        Bracelet bracelet = null;
        String sql = "SELECT * FROM bracelet ORDER BY id DESC LIMIT 1";  // Sélectionne la dernière ligne
        try (PreparedStatement pstmt = cnx.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                bracelet = new Bracelet();
                bracelet.setId(rs.getInt("id"));
                bracelet.setBiologicalDataId(rs.getObject("biological_data_id") != null ? rs.getInt("biological_data_id") : null);
                bracelet.setAlertId(rs.getObject("alert_id") != null ? rs.getInt("alert_id") : null);
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
}