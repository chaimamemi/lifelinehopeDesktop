package com.example.sinda.controllers.admin;

import com.example.sinda.HelloApplication;
import com.example.sinda.ConnectionDB.DataBase;
import com.example.sinda.models.AlertS;
import com.example.sinda.models.Bracelet;
import com.example.sinda.services.AlertService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AutomatiqueAlert {
    private Connection connection;
    private final AlertService alertService=new AlertService();


    public AutomatiqueAlert() {
        connection = DataBase.getInstance().getConnection();
    }
    public void CreatAlaert() {
        List<Bracelet> bracelets;
        try {
            bracelets = getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            for (Bracelet b : bracelets) {
                if (b.getBlood_pressure() > (140.0 / 90.0)) {
                    AlertS alert = new AlertS();
                    alert.setBraceletId(b.getId());
                    alert.setTimestamp(LocalDateTime.now());
                    alert.setAlert_type("Blood_pressure");
                    alert.setSeverity("height");
                    alert.setDescription("height");
                    alert.setHandled(true);
                    alert.setUser_id(b.getIduser());
                    alertService.ajouter(alert);
                    goiadminalert();
                }
                if (b.getTemperature() > 37.5) {
                    AlertS alert = new AlertS();
                    alert.setBraceletId(b.getId());
                    alert.setTimestamp(LocalDateTime.now());
                    alert.setAlert_type("temperature");
                    alert.setSeverity("height");
                    alert.setDescription("height");
                    alert.setHandled(true);
                    alert.setUser_id(b.getIduser());
                    alertService.ajouter(alert);
                    showSuccessAlert("Alert ajoutée avec succès.");
                    goiadminalert();
                }
                if (b.getHeart_rate() > 80) {
                    AlertS alert = new AlertS();
                    alert.setBraceletId(b.getId());
                    alert.setTimestamp(LocalDateTime.now());
                    alert.setAlert_type("Heart_rate");
                    alert.setSeverity("height");
                    alert.setDescription("height");
                    alert.setHandled(true);
                    alert.setUser_id(b.getIduser());
                    alertService.ajouter(alert);
                    showSuccessAlert("Alert ajoutée avec succès.");
                    goiadminalert();
                }
                if ("None".equals(b.getMovement())) {
                    AlertS alert = new AlertS();
                    alert.setBraceletId(b.getId());
                    alert.setTimestamp(LocalDateTime.now());
                    alert.setAlert_type("mouvement ");
                    alert.setSeverity("none");
                    alert.setDescription("none");
                    alert.setHandled(true);
                    alert.setUser_id(b.getIduser());
                    alertService.ajouter(alert);
                    showSuccessAlert("Alert ajoutée avec succès.");
                    goiadminalert();
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de l'ajout de l'Alert : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Bracelet> getAll() throws SQLException {
        List<Bracelet> bracelets = new ArrayList<>();
        String req = "SELECT * FROM bracelet";
       PreparedStatement os = connection.prepareStatement(req) ;
            ResultSet rs = os.executeQuery();
            while (rs.next()) {
                Bracelet bracelet = new Bracelet();
                bracelet.setId(rs.getInt("id"));
                bracelet.setTemperature(rs.getDouble("temperature"));
                bracelet.setBlood_pressure(rs.getDouble("blood_pressure"));
                bracelet.setHeart_rate(rs.getDouble("heart_rate"));
                bracelet.setMovement(rs.getString("movement"));
                bracelet.setIduser(rs.getInt("iduser"));
                bracelets.add(bracelet);
            }
        return bracelets;
    }

    private void showSuccessAlert(String message) {

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    void goiadminalert() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/admin/Alerts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage alertStage = new Stage();
        alertStage.setTitle("Alert List");
        alertStage.setScene(scene);
        alertStage.show();
    }
}
