package org.example.Controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.models.Medication;

public class UpdateMedicationController {

    @FXML
    private TextField tfNameUpdate;

    @FXML
    private TextField tfDescUpdate;

    @FXML
    private TextField tfDosageUpdate;

    @FXML
    private TextField tfNoteUpdate;

    @FXML
    private Button btnsave; // Ajouter le bouton btnSave

    // Ajouter une référence à la médication en cours de modification
    private Medication currentMedication;

    // Méthode pour initialiser les champs avec les détails de la médication sélectionnée
    public void initData(Medication medication) {
        currentMedication = medication;
        tfNameUpdate.setText(medication.getNameMedication());
        tfDescUpdate.setText(medication.getDescription());
        tfDosageUpdate.setText(medication.getDosage());
        tfNoteUpdate.setText(medication.getMedicalNote());
    }

    // Méthode pour enregistrer les modifications
    @FXML
    private void saveChanges() {
        // Mettre à jour les détails de la médication avec les nouvelles valeurs saisies
        currentMedication.setNameMedication(tfNameUpdate.getText());
        currentMedication.setDescription(tfDescUpdate.getText());
        currentMedication.setDosage(tfDosageUpdate.getText());
        currentMedication.setMedicalNote(tfNoteUpdate.getText());

        // Vous pouvez appeler ici votre méthode de service pour enregistrer les modifications dans la base de données
        // Par exemple :
        // serviceMedication.update(currentMedication);

        // Fermer la fenêtre d'édition après l'enregistrement
        btnsave.getScene().getWindow().hide();
    }
}
