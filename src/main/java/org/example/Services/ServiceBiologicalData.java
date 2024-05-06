package org.example.Services;
import org.example.Interfaces.Iservice;
import org.example.connectionDB.DatabaseConnector;
import org.example.models.BiologicalData;
import java.sql.*;
import java.util.ArrayList;

public class ServiceBiologicalData implements Iservice<BiologicalData> {

    private Connection cnx;

    public ServiceBiologicalData() {
        cnx = DatabaseConnector.getInstance().getCnx();
    }

    @Override
    public void add(BiologicalData data) {
        String sql = "INSERT INTO biological_data (id, measurement_type, value, patient_name, patient_last_name, patient_age, disease,other_information) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, data.getId());
            pstmt.setString(2, data.getMeasurementType());
            pstmt.setString(3, data.getValue());
            pstmt.setString(4, data.getPatientName());
            pstmt.setString(5, data.getPatientLastName());
            pstmt.setInt(6, data.getPatientAge());
            pstmt.setString(7, data.getDisease());
            if (data.getOtherInformation() != null) {
                pstmt.setString(8, data.getOtherInformation());
            } else {
                pstmt.setString(8, ""); // Ou une valeur par défaut
            }



            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'insertion de données biologiques a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("ID généré pour les données biologiques : " + generatedId);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID généré pour les données biologiques.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<BiologicalData> getAll() {
        ArrayList<BiologicalData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM biological_data";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BiologicalData data = new BiologicalData(
                        rs.getInt("id"),
                        rs.getString("measurement_type"),
                        rs.getString("value"),
                        rs.getString("patient_name"),
                        rs.getString("patient_last_name"),
                        rs.getInt("patient_age"),
                        rs.getString("disease"),
                        rs.getString("other_information")
                );





                dataList.add(data);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dataList;
    }


    @Override
    public void update(BiologicalData biologicalData) {
        String sql = "UPDATE biological_data SET measurement_type = ?, value = ?, patient_name = ?, patient_last_name = ?, patient_age = ?, disease = ?, other_information = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, biologicalData.getMeasurementType());
            pstmt.setString(2, biologicalData.getValue());
            pstmt.setString(3, biologicalData.getPatientName());
            pstmt.setString(4, biologicalData.getPatientLastName());
            pstmt.setInt(5, biologicalData.getPatientAge());
            pstmt.setString(6, biologicalData.getDisease());
            pstmt.setString(7, biologicalData.getOtherInformation());
            pstmt.setInt(8, biologicalData.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Les données biologiques ont été mises à jour avec succès.");
            } else {
                System.out.println("Aucune donnée biologique mise à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour des données biologiques : " + e.getMessage());
        }
    }


    @Override
    public boolean delete(BiologicalData data) {
        String sql = "DELETE FROM biological_data WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, data.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

