package com.example.sinda.controllers.user;

import com.example.sinda.HelloApplication;
import com.example.sinda.models.AlertS;
import com.example.sinda.models.Intervention;
import com.example.sinda.services.InterventionService;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ALertIntervention {
    @FXML
    private Label Datef;

    @FXML
    private Text DescreptionF;

    @FXML
    private ListView<Intervention> listeview;

    @FXML
    private Label severityF;

    @FXML
    private Label typrF;
    private final InterventionService interventionService = new InterventionService();
    @FXML
    private TextField recherchefld;
    List<Intervention> listerecherche=new ArrayList<>();
    AlertS alert=new AlertS();
    @FXML
    void initialize(AlertS alert) {
        this.alert=alert;
        recherchefld.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if(recherchefld.getText().isEmpty()){displayIntervention(listerecherche);}
            else{List<Intervention> interventions;
                interventions=rechercher(listerecherche,newValue);
                displayIntervention(interventions);}
        });
        Datef.setText(Datef.getText()+" "+alert.getTimestamp());
        DescreptionF.setText(DescreptionF.getText()+" "+alert.getDescription());
        severityF.setText(severityF.getText()+" "+alert.getSeverity());
        typrF.setText(typrF.getText()+" "+alert.getAlert_type());
        try {
            displayIntervention(interventionService.getByIdUser(alert.getId()));
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de la chargement de l'Intervention : " + e.getMessage());
        }
    }
    public static List<Intervention> rechercher(List<Intervention> liste, String recherche) {
        List<Intervention> resultats = new ArrayList<>();

        for (Intervention element : liste) {
            if (element.getAction().contains(recherche)) {
                resultats.add(element);
            }
        }

        return resultats;
    }
    void  getlist(){
        try {
            listerecherche = interventionService.getByIdUser(this.alert.getId());
        } catch (SQLException e) {
            System.out.println("ccc"+e.getMessage());
        }
    }
    public void displayIntervention(List<Intervention> interventions) {
        afficherInterventions();
        listeview.getItems().setAll(interventions);
    }
    void afficherInterventions() {
        listeview.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Intervention> call(ListView<Intervention> param) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox();
                    private final HBox h1 = new HBox();
                    private final Label dateLabel = new Label();
                    private final Label actionLabel = new Label();
                    private final Label otherActionLabel = new Label();

                    {
                        h1.setSpacing(10);
                        hbox.setSpacing(10);
                        hbox.getChildren().addAll(h1);

                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }

                    @Override
                    protected void updateItem(Intervention item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            dateLabel.setText("Date: " + item.getDateTime().toString());
                            actionLabel.setText("Action: " + item.getAction());
                            otherActionLabel.setText("Other ActionLabel"+item.getOtherAction());
                            h1.getChildren().setAll(dateLabel, actionLabel,otherActionLabel);

                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void goListAlert() throws IOException {
        Scene scenefer = severityF.getScene();
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
