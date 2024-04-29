package com.example.sinda.services;

import com.example.sinda.ConnectionDB.DataBase;
import com.example.sinda.models.Intervention;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterventionService implements Iservice<Intervention> {
    private Connection connection;

    public InterventionService() {
        connection = DataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Intervention intervention) throws SQLException {
        String req = "INSERT INTO intervention_action (date_time, action, other_action, patient_id, emergency_team_id, alert_id) VALUES (?, ?, ?, ?, ?, ?)";
         connection = DataBase.getInstance().getConnection();
             PreparedStatement os = connection.prepareStatement(req);
            os.setTimestamp(1, Timestamp.valueOf(intervention.getDateTime()));
            os.setString(2, intervention.getAction());
            os.setString(3, intervention.getOtherAction());
            os.setInt(4, intervention.getPatientId());
            os.setInt(5, intervention.getEmergencyTeamId());
            os.setInt(6, intervention.getAlertId());
            os.executeUpdate();
            System.out.println("Intervention ajoutée avec succès");

    }

    @Override
    public void modifier(Intervention intervention) throws SQLException {
        String req = "UPDATE intervention_action SET date_time = ?, action = ?, other_action = ?, patient_id = ?, emergency_team_id = ?, alert_id = ? WHERE id = ?";
         connection = DataBase.getInstance().getConnection();
             PreparedStatement os = connection.prepareStatement(req);
            os.setTimestamp(1, Timestamp.valueOf(intervention.getDateTime()));
            os.setString(2, intervention.getAction());
            os.setString(3, intervention.getOtherAction());
            os.setInt(4, intervention.getPatientId());
            os.setInt(5, intervention.getEmergencyTeamId());
            os.setInt(6, intervention.getAlertId());
            os.setInt(7, intervention.getId());
            os.executeUpdate();
            System.out.println("Intervention modifiée avec succès");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM intervention_action WHERE id = ?";
       connection = DataBase.getInstance().getConnection();
             PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, id);
            os.executeUpdate();
            System.out.println("Intervention supprimée avec succès");

    }

    @Override
    public Intervention getOneById(int id) throws SQLException {
        String req = "SELECT * FROM intervention_action WHERE alert_id = ? ORDER BY id DESC";
         connection = DataBase.getInstance().getConnection();
             PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, id);
            ResultSet rs = os.executeQuery();
            if (rs.next()) {
                Intervention intervention = new Intervention();
                intervention.setId(rs.getInt("id"));
                intervention.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                intervention.setAction(rs.getString("action"));
                intervention.setOtherAction(rs.getString("other_action"));
                intervention.setPatientId(rs.getInt("patient_id"));
                intervention.setEmergencyTeamId(rs.getInt("emergency_team_id"));
                intervention.setAlertId(rs.getInt("alert_id"));
                return intervention;
            }

        return null;
    }

    @Override
    public List<Intervention> getAll() throws SQLException {
        List<Intervention> interventions = new ArrayList<>();
        String req = "SELECT * FROM intervention_action";
         connection = DataBase.getInstance().getConnection();
             PreparedStatement os = connection.prepareStatement(req);
            ResultSet rs = os.executeQuery();
            while (rs.next()) {
                Intervention intervention = new Intervention();
                intervention.setId(rs.getInt("id"));
                intervention.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                intervention.setAction(rs.getString("action"));
                intervention.setOtherAction(rs.getString("other_action"));
                intervention.setPatientId(rs.getInt("patient_id"));
                intervention.setEmergencyTeamId(rs.getInt("emergency_team_id"));
                intervention.setAlertId(rs.getInt("alert_id"));
                interventions.add(intervention);

        }
        return interventions;
    }

    @Override
    public List<Intervention> getByIdUser(int id) throws SQLException {
        List<Intervention> interventions = new ArrayList<>();
        String req = "SELECT * FROM intervention_action WHERE alert_id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setInt(1, id);
        ResultSet rs = os.executeQuery();
        while (rs.next()) {
            Intervention intervention = new Intervention();
            intervention.setId(rs.getInt("id"));
            intervention.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            intervention.setAction(rs.getString("action"));
            intervention.setOtherAction(rs.getString("other_action"));
            intervention.setPatientId(rs.getInt("patient_id"));
            intervention.setEmergencyTeamId(rs.getInt("emergency_team_id"));
            intervention.setAlertId(rs.getInt("alert_id"));
            interventions.add(intervention);

        }
        return interventions;
}}
