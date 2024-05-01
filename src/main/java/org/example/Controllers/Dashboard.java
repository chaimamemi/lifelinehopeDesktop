package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import org.example.models.Medication;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    @FXML
    private HBox cardLayout;
    @FXML
    private Button btnstat;

    @FXML
    private Button btnpd;
    private List<Medication> recentlyAdded;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnstat.setOnAction(event -> showStatistics());



    }
    @FXML
    private void showStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistics.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre pour afficher les statistiques
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Medication Statistics");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

