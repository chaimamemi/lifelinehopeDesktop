package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.models.BiologicalData;
import org.example.models.Medication;
import org.w3c.dom.Text;

public class UpdateBiological {

@FXML
private TextField  Tfmupdate;
    @FXML
    private TextField  Tfup;
    @FXML
    private TextField Tfn;
    @FXML
    private  TextField Tfl;

    @FXML
    private  TextField TfA;


    @FXML
    private Button btnsave;
    private BiologicalData selectedBiologicalData;


    public void initData(BiologicalData data) {
        selectedBiologicalData = data;
        Tfmupdate.setText(data.getMeasurementType());
        Tfup.setText(data.getValue());
        Tfn.setText(data.getPatientName());
        Tfl.setText(data.getPatientLastName());

    }

    @FXML
    private void saveChanges() {
        // Mettre à jour les détails de la médication avec les nouvelles valeurs saisies
       selectedBiologicalData.setMeasurementType(Tfmupdate.getText());
        selectedBiologicalData.setValue(Tfup.getText());
       selectedBiologicalData.setPatientName(Tfn.getText());
        selectedBiologicalData.setPatientLastName(Tfl.getText());
        selectedBiologicalData.setPatientLastName(TfA.getText());




        btnsave.getScene().getWindow().hide();
    }

    public void saveBiological(ActionEvent actionEvent) {
    }
}
