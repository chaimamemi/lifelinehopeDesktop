package com.example.sinda.services;

import com.example.sinda.ConnectionDB.DataBase;
import com.example.sinda.models.AlertS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertService implements Iservice<AlertS> {
    private Connection connection;

    public AlertService() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(AlertS alert) throws SQLException {
        String req = "INSERT INTO alert (bracelet_id, timestamp, alert_type, severity, description, handled, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, alert.getBraceletId());
            os.setTimestamp(2, Timestamp.valueOf(alert.getTimestamp()));
            os.setString(3, alert.getAlert_type());
            os.setString(4, alert.getSeverity());
            os.setString(5, alert.getDescription());
            os.setBoolean(6, alert.isHandled());
            os.setInt(7, alert.getUser_id());
            os.executeUpdate();
            System.out.println("Alerte ajoutée avec succès");

    }

    @Override
    public void modifier(AlertS alert) throws SQLException {
        String req = "UPDATE alert SET bracelet_id = ?, timestamp = ?, alert_type = ?, severity = ?, description = ?, handled = ?, user_id = ? WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, alert.getBraceletId());
            os.setTimestamp(2, Timestamp.valueOf(alert.getTimestamp()));
            os.setString(3, alert.getAlert_type());
            os.setString(4, alert.getSeverity());
            os.setString(5, alert.getDescription());
            os.setBoolean(6, alert.isHandled());
            os.setInt(7, alert.getUser_id());
            os.setInt(8, alert.getId());
            os.executeUpdate();
            System.out.println("Alerte modifiée avec succès");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM alert WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, id);
            os.executeUpdate();
            System.out.println("Alerte supprimée avec succès");

    }

    @Override
    public AlertS getOneById(int id) throws SQLException {
        String req = "SELECT * FROM alert WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, id);
            ResultSet rs = os.executeQuery();
            while (rs.next()) {
                AlertS alert = new AlertS();
                alert.setId(rs.getInt("id"));
                alert.setBraceletId(rs.getInt("bracelet_id"));
                alert.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                alert.setAlert_type(rs.getString("alert_type"));
                alert.setSeverity(rs.getString("severity"));
                alert.setDescription(rs.getString("description"));
                alert.setHandled(rs.getBoolean("handled"));
                alert.setUser_id(rs.getInt("user_id"));
                return alert;
            }

        return null;
    }

    @Override
    public List<AlertS> getAll() throws SQLException {
        List<AlertS> alerts = new ArrayList<>();
        String req = "SELECT * FROM alert ORDER BY id DESC";
        PreparedStatement os = connection.prepareStatement(req);
            ResultSet rs = os.executeQuery();
            while (rs.next()) {
                AlertS alert = new AlertS();
                alert.setId(rs.getInt("id"));
                alert.setBraceletId(rs.getInt("bracelet_id"));
                alert.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                alert.setAlert_type(rs.getString("alert_type"));
                alert.setSeverity(rs.getString("severity"));
                alert.setDescription(rs.getString("description"));
                alert.setHandled(rs.getBoolean("handled"));
                alert.setUser_id(rs.getInt("user_id"));
                alerts.add(alert);

        }
        return alerts;
    }

    @Override
    public List<AlertS> getByIdUser(int id) throws SQLException {
        List<AlertS> alerts = new ArrayList<>();
        String req = "SELECT * FROM alert WHERE user_id = ?";
        PreparedStatement os = connection.prepareStatement(req);
            os.setInt(1, id);
            ResultSet rs = os.executeQuery();
            while (rs.next()) {
                AlertS alert = new AlertS();
                alert.setId(rs.getInt("id"));
                alert.setBraceletId(rs.getInt("bracelet_id"));
                alert.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                alert.setAlert_type(rs.getString("alert_type"));
                alert.setSeverity(rs.getString("severity"));
                alert.setDescription(rs.getString("description"));
                alert.setHandled(rs.getBoolean("handled"));
                alert.setUser_id(rs.getInt("user_id"));
                alerts.add(alert);

        }
        return alerts;
    }
}
