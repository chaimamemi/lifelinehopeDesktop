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
    private Button btnsave;

    private Medication currentMedication;

    public void initData(Medication medication) {
        currentMedication = medication;
        tfNameUpdate.setText(medication.getNameMedication());
        tfDescUpdate.setText(medication.getDescription());
        tfDosageUpdate.setText(medication.getDosage());
        tfNoteUpdate.setText(medication.getMedicalNote());
    }

    @FXML
    private void saveChanges() {
        currentMedication.setNameMedication(tfNameUpdate.getText());
        currentMedication.setDescription(tfDescUpdate.getText());
        currentMedication.setDosage(tfDosageUpdate.getText());
        currentMedication.setMedicalNote(tfNoteUpdate.getText());

        // Vous pouvez appeler ici votre méthode de service pour enregistrer les modifications dans la base de données
        // Par exemple :
        // serviceMedication.update(currentMedication);

        btnsave.getScene().getWindow().hide();
    }
}
