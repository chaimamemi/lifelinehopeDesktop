package org.example.Controllers.user;

import com.google.zxing.WriterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.Controllers.GenerateQrCode;
import org.example.Services.ServiceMedication;
import org.example.models.Medication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AfficherMedication.fxml"));

        try {
            Parent root = loader.load();
            AfficherMedication ap = loader.getController();
            ap.setMedications(sp.getAll());
            tfName.getScene().setUserData(ap);
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
            String dosageText = tfDosage.getText();
            try {
                double dosageValue = Double.parseDouble(dosageText);
                if (dosageValue > 1000) {
                    showAlert("Erreur de dosage", "Le dosage ne peut pas dépasser 1000.");
                } else {
                    // Concaténer tous les champs dans une seule chaîne de caractères
                    String qrData = "Nom: " + tfName.getText() +
                            "\nDosage: " + tfDosage.getText() +
                            "\nDescription: " + tfDesc.getText() +
                            "\nNote médicale: " + tfNote.getText();

                    Medication m = new Medication();
                    m.setNameMedication(tfName.getText());
                    m.setDescription(tfDesc.getText());
                    m.setDosage(tfDosage.getText());
                    m.setMedicalNote(tfNote.getText());
                    sp.add(m);
                    String uniqueId = UUID.randomUUID().toString();
                    String qrFileName = "src/main/resources/qrcodes/" + tfName.getText() + "_" + uniqueId + ".png";

                    // Créer le code QR
                    GenerateQrCode.createQR(qrData, qrFileName);

                    showAlert("Succès", "Code QR généré pour la médication : " + tfName.getText());



                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AfficherMedication.fxml"));
                    try {
                        Parent root = loader.load();
                        AfficherMedication ap = loader.getController();
                        ap.setMedications(sp.getAll());
                        tfName.getScene().setUserData(ap);
                        tfName.getScene().setRoot(root);
                    } catch (IOException e) {
                        showAlert("Erreur", "Impossible de charger la vue AfficherMedication.");
                        System.out.println(e.getMessage());
                    }
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur de dosage", "Veuillez saisir une valeur numérique valide pour le dosage.");
            } catch (IOException | WriterException e) {
                showAlert("Erreur de génération de QR code", "Une erreur s'est produite lors de la génération du code QR.");
                System.out.println(e.getMessage());
            }
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
