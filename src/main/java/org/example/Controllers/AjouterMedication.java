package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.Services.ServiceMedication;
import org.example.models.Medication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjouterMedication {
    ServiceMedication sp = new ServiceMedication();

    @FXML
    private TextField tfName;


    @FXML
    private TextField tfDosage;

    @FXML
    private TextField tfDesc;

    @FXML
    private TextField tfNote;

    @FXML
    private Button btnajout;

    @FXML
    private Button btnaff;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnajout.setOnAction(this::ajouterMedication);
        btnaff.setOnAction(this::affichierMedication);
    }

    @FXML
    void affichierMedication(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherMedication.fxml"));

        try {
            Parent root = loader.load();
            AfficherMedication ap = loader.getController();

            tfName.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue AfficherMedication.");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void ajouterMedication(ActionEvent event) {
        if (areFieldsEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs.");
        } else {
            Medication m = new Medication();
            m.setNameMedication(tfName.getText());
            m.setDescription(tfDesc.getText());
            m.setDosage(tfDosage.getText());
            m.setMedicalNote(tfNote.getText());
            sp.add(m);
        }
    }

    private boolean areFieldsEmpty() {
        return tfName.getText().isEmpty() || tfDesc.getText().isEmpty() || tfDosage.getText().isEmpty() || tfNote.getText().isEmpty();
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
