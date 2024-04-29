package com.example.sinda.controllers.user;

import com.example.sinda.HelloApplication;
import com.example.sinda.models.AlertS;
import com.example.sinda.services.AlertService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ModifierAlert {
    @FXML
    private ComboBox<String> Cmbotype;

    @FXML
    private TextArea descriptionF;

    @FXML
    private TextField serverityF;

    @FXML
    private Label checkdecreption;

    @FXML
    private Label checkseverity;

    @FXML
    private Label checktype;
    private AlertS alert=new AlertS();
    private final AlertService alertService=new AlertService();
    int bracelletid=1;
    int userid=1;
    @FXML
    void initialize(AlertS alert) {
        this.alert=alert;
        Cmbotype.setValue(alert.getAlert_type());
        descriptionF.setText(alert.getDescription());
        serverityF.setText(alert.getSeverity());
        ObservableList<String> options = FXCollections.observableArrayList(
                "Temperature",
                "Blood pressure",
                "Heart Rate",
                "Movement"
        );
        Cmbotype.setItems(options);

    }
    private boolean validateInput() {
        String type = Cmbotype.getValue();
        String serverity  = serverityF.getText();
        String description = descriptionF.getText();

        if (Cmbotype.getValue()==null) {
            checktype.setVisible(true);
            return false;
        }
        if(serverity == null){
            checkseverity.setVisible(true);
            return false;
        }
        if (description.isEmpty()) {
            checkdecreption.setVisible(true);
            return false;
        }

        return true;
    }
    @FXML
    void modifierAlerte(){
        validateInput();
        if (validateInput()){
            this.alert.setAlert_type(Cmbotype.getValue());
            this.alert.setSeverity(serverityF.getText());
            this.alert.setDescription(descriptionF.getText());
            this.alert.setHandled(true);
            try {
                alertService.modifier(this.alert);
                showSuccessAlert("Alert ajoutée avec succès.");
                Scene scenefer = serverityF.getScene();
                Stage stagefer = (Stage) scenefer.getWindow();
                stagefer.close();
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/User/Listealert.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("");
                stage.setScene(scene);
                stage.show();

            } catch (SQLException e) {
                showErrorAlert("Erreur lors de l'ajout de l'Alert : " + e.getMessage());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
    @FXML
    void goListAlert() throws IOException {
        Scene scenefer = Cmbotype.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/User/Listealert.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alert List");
        stage.setScene(scene);
        stage.show();
    }

}
