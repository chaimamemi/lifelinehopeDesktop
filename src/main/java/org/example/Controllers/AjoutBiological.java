package org.example.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class AjoutBiological  {
    ServiceBiologicalData sp = new ServiceBiologicalData();
    @FXML
    private TextField idb;
    @FXML
    private TextField ftM;
    @FXML
    private TextField lbBio;
    @FXML
    private TextField Ftv;

    @FXML
    private TextField FtP;
    @FXML
    private TextField Ftd;

    @FXML
    private Button btnshow;

    @FXML
    private TextField fTl;
    @FXML
    Button btnadd;

    @FXML
    private TextField FtA;
    @FXML
    private TextField FtOth;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnadd.setOnAction(this::ajouterBiological);
        btnshow.setOnAction(this::afficherBiological);
    }


    @FXML
    private void ajouterBiological(ActionEvent actionEvent) {
        if (areFieldsEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs.");
        } else {
            BiologicalData data = new BiologicalData();

            data.setId(Integer.parseInt(idb.getText()));
            data.setPatientAge(Integer.parseInt(FtA.getText()));
            data.setMeasurementType(ftM.getText());
            data.setValue(Ftv.getText());
            data.setPatientName(FtP.getText());
            data.setPatientLastName(fTl.getText());
            data.setDisease(Ftd.getText());

            sp.add(data);

            // Charger le fichier FXML AjoutBiological.fxml après l'ajout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutBiological.fxml"));
            try {
                Parent root = loader.load();
                AjoutBiological controller = loader.getController();
               idb.getScene().setRoot(root);
            } catch (IOException e) {
                showAlert("Erreur", "Impossible de charger la vue AjoutBiological.");
                System.out.println(e.getMessage());
            }
        }
    }



    private boolean areFieldsEmpty() {

        return ftM == null || ftM.getText().isEmpty() || Ftv.getText().isEmpty() || FtP.getText().isEmpty() || fTl.getText().isEmpty() || FtA.getText().isEmpty() || Ftd.getText().isEmpty();
    }


    @FXML
    void afficherBiological(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherBiological.fxml"));

        try {
            Parent root = loader.load();
            AjoutBiological controller = loader.getController();
            idb.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue AjoutBiological.");
            System.out.println(e.getMessage());
        }
    }


    {

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}







