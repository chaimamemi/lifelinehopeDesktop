package com.example.sinda.controllers.admin;

import com.example.sinda.HelloApplication;
import com.example.sinda.models.AlertS;
import com.example.sinda.models.Intervention;
import com.example.sinda.services.InterventionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class ShowIntervention {

    @FXML
    private TableView<Intervention> tableView;
    @FXML
    private Button Addbtn;

    @FXML
    private Label Datef;

    @FXML
    private Text DescreptionF;

    @FXML
    private Button Editbtn;

    @FXML
    private TextArea Intervention_OactionFF;

    @FXML
    private ComboBox<String> Intervention_actionF;


    @FXML
    private Label checkAction;

    @FXML
    private Label checkotheraction;

    @FXML
    private Pane pane;

    @FXML
    private Label severityF;



    @FXML
    private Label typrF;

    private final InterventionService interventionService = new InterventionService();

    AlertS alertS;
@FXML
    void initialize(AlertS alert){
    ObservableList<String> options = FXCollections.observableArrayList(
            "Cool fever: Wet cloth or fan",
            "Position: Ensure stable blood pressure",
            "Medicine: Give for high temperature.",
            "Breathe: Calm exercises for fast heart rate",
            "Move: Regularly for circulation",
            "Peace: Quiet for heart rate",
            "Exercise: Prevent stiffness"
    );
    Intervention_actionF.setItems(options);

    this.alertS=alert;
    Datef.setText(Datef.getText()+" "+alert.getTimestamp());
    DescreptionF.setText(DescreptionF.getText()+" "+alert.getDescription());
    severityF.setText(severityF.getText()+" "+alert.getSeverity());
    typrF.setText(typrF.getText()+" "+alert.getAlert_type());
    refreshInterventions();
    tableView.setStyle(
            "-fx-background-color: #EFFCFF; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-border-color: #9CCBD6; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-border-width: 1px; ");
}
    private void refreshInterventions() {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<Intervention, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        TableColumn<Intervention, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        TableColumn<Intervention, String> otherActionCol = new TableColumn<>("Other Action");
        otherActionCol.setCellValueFactory(new PropertyValueFactory<>("otherAction"));

        TableColumn<Intervention, Integer> patientIdCol = new TableColumn<>("Patient ID");
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<Intervention, Integer> emergencyTeamIdCol = new TableColumn<>("Emergency Team ID");
        emergencyTeamIdCol.setCellValueFactory(new PropertyValueFactory<>("emergencyTeamId"));

        TableColumn<Intervention, Integer> alertIdCol = new TableColumn<>("Alert ID");
        alertIdCol.setCellValueFactory(new PropertyValueFactory<>("alertId"));

        TableColumn<Intervention, Void> editcol = new TableColumn<>("Edit");
        editcol.setCellFactory(param -> new TableCell<>() {
            private final Button editbtn = new Button("Edit");

            {
                editbtn.setOnAction(event -> {
                    Intervention intervention = getTableView().getItems().get(getIndex());
                    pane.setVisible(true);
                    Editbtn.setVisible(true);
                    modifier(intervention);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editbtn);
                }
            }
        });

        TableColumn<Intervention, Void> deleteCol = new TableColumn<>("Delet");
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Intervention intervention = getTableView().getItems().get(getIndex());
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation de suppression");
                    confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cette intervention ?");
                    confirmationAlert.setContentText("Cette action est irréversible.");

                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            interventionService.supprimer(intervention.getId());
                            tableView.refresh();
                            refreshInterventions();
                        } catch (SQLException e) {
                            showErrorAlert("Erreur lors de la suppression de l'intervention : " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        tableView.getColumns().addAll(dateTimeCol, actionCol, otherActionCol, patientIdCol, emergencyTeamIdCol, alertIdCol, deleteCol,editcol);

        try {
            tableView.getItems().addAll(interventionService.getByIdUser(this.alertS.getId()));
        } catch (SQLException e) {
            showErrorAlert("Erreur lors du chargement des interventions : " + e.getMessage());
        }
    }
    private void showSuccessAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void goBack() throws IOException {
        Scene currentScene = tableView.getScene();
        Stage currentStage = (Stage) currentScene.getWindow();
        currentStage.close();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin/Alerts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Dones");
        stage.show();
    }

    void modifier(Intervention intervention){
        Intervention_actionF.setValue(intervention.getAction());
        Intervention_OactionFF.setText(intervention.getOtherAction());
        Editbtn.setOnAction(event->{
            validateInput();
            if(validateInput()){
                intervention.setAction(Intervention_actionF.getValue());
                intervention.setOtherAction(Intervention_OactionFF.getText());
                try {
                    interventionService.modifier(intervention);
                    Intervention_actionF.setValue("");
                    Intervention_OactionFF.setText("");
                    Editbtn.setVisible(false);
                    pane.setVisible(false);
                    showSuccessAlert("Intervention modifier avec succès.");
                    refreshInterventions();
                } catch (SQLException e) {
                    showErrorAlert("Erreur lors de la modification de l'Intervention : " + e.getMessage());
                }

            }
        });
    }
    @FXML
    void Ajouter(){
        validateInput();
        if(validateInput()){
            Intervention intervention=new Intervention();
            intervention.setAction(Intervention_actionF.getValue());
            intervention.setOtherAction(Intervention_OactionFF.getText());
            intervention.setDateTime(LocalDateTime.now());
            intervention.setPatientId(this.alertS.getUser_id());
            System.out.println(this.alertS.getUser_id());
            intervention.setEmergencyTeamId(1);
            intervention.setAlertId(this.alertS.getId());
            try {
                interventionService.ajouter(intervention);
                Intervention_actionF.setValue("");
                Intervention_OactionFF.setText("");
                Addbtn.setVisible(false);
                pane.setVisible(false);
                showSuccessAlert("Intervention ajoutée avec succès.");
                refreshInterventions();
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de l'ajout de l'Intervention : " + e.getMessage());
            }

        }
    }
    private boolean validateInput() {
        if(Intervention_actionF.getValue().isEmpty()){
            checkAction.setVisible(true);
            return false;
        }
        if (Intervention_OactionFF.getText().isEmpty()) {
            checkotheraction.setVisible(true);
            return false;
        }

        return true;
    }
    @FXML
    void showaddpan(){
    pane.setVisible(true);
    Addbtn.setVisible(true);
    }
    @FXML
    void goListAlert() throws IOException {
        Scene scenefer = Intervention_actionF.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/admin/Alerts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alert List");
        stage.setScene(scene);
        stage.show();
    }
}
