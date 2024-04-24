



import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.Medication;

public class UpdateMedication {

    @FXML
    private TextField tfNameUpdate;

    @FXML
    private TextField tfDescUpdate;

    @FXML
    private TextField tfDosageUpdate;

    @FXML
    private TextField tfNoteUpdate;

    // Méthode pour initialiser les champs avec les détails de la médication sélectionnée
    public void initData(Medication medication) {
        tfNameUpdate.setText(medication.getNameMedication());
        tfDescUpdate.setText(medication.getDescription());
        tfDosageUpdate.setText(medication.getDosage());
        tfNoteUpdate.setText(medication.getMedicalNote());
    }
}
