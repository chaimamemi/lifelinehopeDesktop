package com.example.sinda.controllers.user;

import com.example.sinda.HelloApplication;
import com.example.sinda.models.Intervention;
import com.example.sinda.models.AlertS;
import com.example.sinda.services.AlertService;
import com.example.sinda.services.InterventionService;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ListealertController {
    @FXML
    private ScrollPane ScrollePan;
    private final AlertService alertService=new AlertService();
    private final InterventionService interventionService=new InterventionService();
    int user_id=1;
    @FXML
    private TextField recherchefld;
    List<AlertS> listerecherche=new ArrayList<>();
    @FXML
    public void initialize() {
       getlist();
        recherchefld.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if(recherchefld.getText().isEmpty()){loadDonations(listerecherche);}
                else{List<AlertS> series;
            series=rechercher(listerecherche,newValue);
            loadDonations(series);}
        });
        loadDonations(listerecherche);
    }
    public static List<AlertS> rechercher(List<AlertS> liste, String recherche) {
        List<AlertS> resultats = new ArrayList<>();

        for (AlertS element : liste) {
            if (element.getDescription().contains(recherche) ||element.getAlert_type().contains(recherche)||element.getSeverity().contains(recherche) ) {
                resultats.add(element);
            }
        }

        return resultats;
    }
    void  getlist(){
        try {
            listerecherche = alertService.getByIdUser(user_id);
        } catch (SQLException e) {
            System.out.println("ccc"+e.getMessage());
        }
    }
    private void loadDonations(List<AlertS> alertS) {
        FlowPane donationFlowPane = new FlowPane();
        donationFlowPane.setStyle("-fx-pref-width: 450px; " +
                "-fx-pref-height: 547px;"+
                "-fx-background-color: rgba(0, 0, 8, 0); ");


        for (AlertS alertS1 : alertS) {
                VBox cardContainer = createDonationVBox(alertS1);
                donationFlowPane.getChildren().add(cardContainer);
                FlowPane.setMargin(cardContainer, new Insets(10));
            }
            ScrollePan.setContent(donationFlowPane);

        }

    private VBox createDonationVBox(AlertS alert) {
        VBox cardContainer = new VBox();
        cardContainer.setStyle("-fx-padding: 10px 10px 10px 10px;");
        cardContainer.setStyle(
                "-fx-background-color: #EFFCFF; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-border-color: #9CCBD6; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-width: 1px; ");


        Pane pane = new Pane();
        pane.setLayoutX(403.0);
        pane.setLayoutY(130.0);
        pane.setPrefHeight(300.0);
        pane.setPrefWidth(440.0);



        Label userLabel = new Label();
        userLabel.setLayoutX(43.0);
        userLabel.setLayoutY(15.0);
        userLabel.setPrefHeight(17.0);
        userLabel.setPrefWidth(200.0);
        userLabel.setText("Date :"+alert.getTimestamp());

        Label DateLabel = new Label();
        DateLabel.setLayoutX(43.0);
        DateLabel.setLayoutY(35.0);
        DateLabel.setPrefHeight(17.0);
        DateLabel.setPrefWidth(200.0);
        DateLabel.setText("Type :"+alert.getAlert_type());

        Label dateLabel = new Label();
        dateLabel.setLayoutX(43.0);
        dateLabel.setLayoutY(55.0);
        dateLabel.setPrefHeight(17.0);
        dateLabel.setPrefWidth(99.0);
        dateLabel.setText(String.valueOf("severity :"+alert.getSeverity()));

        Label contentLabel = new Label();
        contentLabel.setLayoutX(43.0);
        contentLabel.setLayoutY(75.0);
        contentLabel.setPrefHeight(75.0);
        contentLabel.setPrefWidth(538.0);
        contentLabel.setText("Descreption : "+alert.getDescription());
        Intervention c;
        try {
            c=interventionService.getOneById(alert.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Label commentLabel = new Label();
        commentLabel.setLayoutX(95.0);
        commentLabel.setLayoutY(170.0);
        commentLabel.setPrefHeight(17.0);
        commentLabel.setPrefWidth(490.0);

        Label commentDataLabel = new Label();
        commentDataLabel.setLayoutX(115.0);
        commentDataLabel.setLayoutY(190.0);
        commentDataLabel.setPrefHeight(42.0);
        commentDataLabel.setPrefWidth(490.0);
        if(c!=null){commentLabel.setText("Lasted Intervention at "+String.valueOf(c.getDateTime()));
                    commentDataLabel.setText("action :"+c.getAction()+"   "+"Other Action : "+c.getOtherAction());}

        ImageView editImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/edit.png"))));
        editImageView.setFitHeight(42.0);
        editImageView.setFitWidth(35.0);
        editImageView.setLayoutX(360.0);
        editImageView.setLayoutY(10.0);
        editImageView.setPickOnBounds(true);
        editImageView.setPreserveRatio(true);

        editImageView.setOnMouseClicked(event -> {
            modifieralert(alert);
        });
        /////
        ImageView daitailleImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/projects.png"))));
        daitailleImageView.setFitHeight(42.0);
        daitailleImageView.setFitWidth(35.0);
        daitailleImageView.setLayoutX(401.0);
        daitailleImageView.setLayoutY(10.0);
        daitailleImageView.setPickOnBounds(true);
        daitailleImageView.setPreserveRatio(true);

        daitailleImageView.setOnMouseClicked(event -> {
            daitaillealert(alert);
        });
        /////

        ImageView deleteImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/delete.png"))));
        deleteImageView.setFitHeight(35.0);
        deleteImageView.setFitWidth(42.0);
        deleteImageView.setLayoutX(319.0);
        deleteImageView.setLayoutY(10.0);
        deleteImageView.setPickOnBounds(true);
        deleteImageView.setPreserveRatio(true);

        deleteImageView.setOnMouseClicked(event -> {
            Alert alertv = new Alert(Alert.AlertType.CONFIRMATION);
            alertv.setTitle("Confirmation de suppression");
            alertv.setHeaderText("Voulez-vous vraiment supprimer cette Feedback ?");
            alertv.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alertv.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    alertService.supprimer(alert.getId());
                    loadDonations(listerecherche);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
            pane.getChildren().addAll(userLabel,DateLabel,
                    dateLabel, contentLabel, commentLabel, commentDataLabel, editImageView, deleteImageView,daitailleImageView);


        cardContainer.getChildren().add(pane);
        return cardContainer;
    }
    void modifieralert(AlertS alert){
        Scene scene1 = ScrollePan.getScene();
        Stage stage1 = (Stage) scene1.getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/ModifierAlaert.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            ModifierAlert controller = loader.getController();
            controller.initialize(alert);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Alert");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void daitaillealert(AlertS alert){
        Scene scene1 = ScrollePan.getScene();
        Stage stage1 = (Stage) scene1.getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AlertIntervention.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            ALertIntervention controller = loader.getController();
            controller.initialize(alert);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Alert Intervention");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goListAlert() throws IOException {
        Scene scenefer = ScrollePan.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/User/Listealert.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alert List");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void goAddAlert() throws IOException {
        Scene scenefer = ScrollePan.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/User/AddAlert.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alert Add");
        stage.setScene(scene);
        stage.show();
    }
}
