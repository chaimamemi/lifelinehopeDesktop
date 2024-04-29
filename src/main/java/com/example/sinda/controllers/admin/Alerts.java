package com.example.sinda.controllers.admin;

import com.example.sinda.controllers.user.ModifierAlert;
import com.example.sinda.models.AlertS;
import com.example.sinda.services.AlertService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class Alerts {

    @FXML
    private TableView<AlertS> tableview;

    private final AlertService alertService = new AlertService();

    @FXML
    public void initialize() {
        refreshAlerts();

    }

    private void refreshAlerts() {
        tableview.getItems().clear();
        tableview.getColumns().clear();

        TableColumn<AlertS, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("alert_type"));

        TableColumn<AlertS, String> severityCol = new TableColumn<>("Severity");
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));

        TableColumn<AlertS, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<AlertS, String> handledCol = new TableColumn<>("Handled");
        handledCol.setCellValueFactory(new PropertyValueFactory<>("handled"));

        TableColumn<AlertS, Void> actionCol = new TableColumn<>("Interventions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Show");

            {
                button.setOnAction(event -> {
                    AlertS alert = getTableView().getItems().get(getIndex());
                   gointervention(alert);
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

        tableview.getColumns().addAll(typeCol, severityCol, descriptionCol, handledCol, actionCol);

        try {
            tableview.getItems().addAll(alertService.getAll());
        } catch (SQLException e) {
            showErrorAlert("Erreur lors du chargement des alertes : " + e.getMessage());
        }
    }
    void gointervention(AlertS alert){
        Scene scene1 = tableview.getScene();
        Stage stage1 = (Stage) scene1.getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/Showintervention.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            ShowIntervention controller = loader.getController();
            controller.initialize(alert);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Alert intervention");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void goBack() throws IOException {
        Scene currentScene = tableview.getScene();
        Stage currentStage = (Stage) currentScene.getWindow();
        currentStage.close();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin/Alerts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Alerts");
        stage.show();
    }
}
