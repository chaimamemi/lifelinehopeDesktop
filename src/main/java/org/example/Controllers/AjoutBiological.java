package org.example.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Services.ServiceBiologicalData;
import org.example.Utils.MyDataBase;
import org.example.models.BiologicalData;

import java.io.IOException;
import java.sql.*;

import javafx.scene.control.Alert.AlertType;
import org.example.models.Medication;

import java.net.URL;
import java.text.BreakIterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AjoutBiological {
    ServiceBiologicalData sp = new ServiceBiologicalData();

    @FXML
    Button btnadd;

    @FXML
    private TextField Ftm;
    @FXML
    private TextField Ftv;
    @FXML
    private TextField Ftn;

    @FXML
    private TextField Ftla;
    @FXML
    private TextField fta;
    @FXML
    private TextField FtD;
    @FXML
    private TextField FtOth;

    @FXML
    private Button btnshow;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnadd.setOnAction(this::ajouterBiological);
        btnshow.setOnAction(this::afficherBiological);
    }

    @FXML
    void afficherBiological(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherBiological.fxml"));
        try {
            Parent root = loader.load();
            AfficherBiological controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue AfficherBiological.");
        }
    }

    @FXML
    void ajouterBiological(ActionEvent event) {
        if (areFieldsEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs.");
        } else {
            BiologicalData data = new BiologicalData();
            data.setMeasurementType(Ftm.getText());
            data.setValue(Ftv.getText());
            data.setPatientName(Ftn.getText());
            data.setPatientLastName(Ftla.getText());
            // Ajout de l'âge sans vérification préalable
            try {
                int age = Integer.parseInt(fta.getText());
                data.setPatientAge(age);
            } catch (NumberFormatException e) {
                showAlert("Erreur de saisie", "Veuillez saisir un nombre valide pour l'âge.");
                return; // Sortir de la méthode si l'âge n'est pas valide
            }

            data.setDisease(FtD.getText());
            data.setOtherInformation(FtOth.getText());
            sp.add(data);
        }
    }


















    private boolean areFieldsEmpty() {

        return Ftm.getText().isEmpty() || Ftv.getText().isEmpty() || Ftn.getText().isEmpty() ||   FtD.getText().isEmpty()||FtOth.getText().isEmpty()||Ftla.getText().isEmpty();
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}







