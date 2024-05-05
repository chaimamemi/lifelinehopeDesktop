package Controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Bracelet;
import services.BraceletService;

import java.io.IOException;
import java.util.List;



public class AfficheAllBraceletController {

    @FXML
    private ListView<String> bloodPresure;

    @FXML
    private VBox bloodp;

    @FXML
    private Button boutonBack;

    @FXML
    private HBox cardBracelet;

    @FXML
    private VBox heartR;

    @FXML
    private ListView<String> hertRate;

    @FXML
    private VBox move;

    @FXML
    private ListView<String> movement;

    @FXML
    private VBox temp;

    @FXML
    private ListView<String> temperatureList;

    @FXML
    private TextArea dataHealth;

    @FXML
    private Button boutonAppoitment;

    @FXML
    private Button boutonGenerate;


    private BraceletService braceletService = new BraceletService();

    @FXML
    void backToBracelet(ActionEvent event) {
        // Assurez-vous que le chemin de ressource est correct et que le fichier FXML est situé dans le dossier de ressources.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageBracelet.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) boutonBack.getScene().getWindow(); // Obtenez la fenêtre actuelle du bouton
            stage.setScene(scene); // Définissez la nouvelle scène chargée pour la fenêtre
            stage.show(); // Affichez la fenêtre avec la nouvelle scène
        } catch (IOException e) {
            e.printStackTrace(); // Imprimez la trace de la pile d'exceptions pour le débogage.
            // Ici, vous pouvez gérer l'exception, comme afficher une alerte à l'utilisateur.
        }
    }



    @FXML
    public void initialize() {
        boutonBack.setOnAction(this::backToBracelet);
        temp();
        heart();
        move();
        blood();
        generateBraceletAnalysis();
        boutonAppoitment.setOnAction(this::makeappoitment);



    }
    private void temp() {
        List<Bracelet> bracelets = braceletService.getAllBracelets(); // Récupérez les données des bracelets.

        for (Bracelet bracelet : bracelets) {
            if (bracelet.getTemperature() != null) {
                try {
                    // Convertissez la chaîne en Double.
                    double temp = Double.parseDouble(bracelet.getTemperature());
                    String formattedTemp = String.format("%.2f°C", temp);
                    temperatureList.getItems().add(formattedTemp);
                } catch (NumberFormatException e) {
                    // Gérez l'exception si la chaîne ne peut pas être convertie en Double.
                    e.printStackTrace();
                }
            }
        }


        temperatureList.setEditable(false);
        temperatureList.setCellFactory(TextFieldListCell.forListView());
    }


    private void heart() {
        List<Bracelet> bracelets = braceletService.getAllBracelets();

        for (Bracelet bracelet : bracelets) {
            if (bracelet.getHeartRate() != null) {
                // Formatez la fréquence cardiaque pour l'affichage.
                String formattedHeartRate = String.format("%.2f bpm", bracelet.getHeartRate());
                hertRate.getItems().add(formattedHeartRate);
            }
        }


        hertRate.setEditable(false);
        hertRate.setCellFactory(TextFieldListCell.forListView());

    }


    private void move() {
        List<Bracelet> bracelets = braceletService.getAllBracelets();

        for (Bracelet bracelet : bracelets) {
            // Ajoutez directement les données de mouvement à la ListView sans avoir besoin de les formater puisqu'elles sont déjà des Strings.
            if (bracelet.getMovement() != null && !bracelet.getMovement().isEmpty()) {
                movement.getItems().add(bracelet.getMovement());
            }
        }
        movement.setEditable(false);
        movement.setCellFactory(TextFieldListCell.forListView());
    }


    private void blood() {
        List<Bracelet> bracelets = braceletService.getAllBracelets();

        for (Bracelet bracelet : bracelets) {
            if (bracelet.getBloodPressure() != null && !bracelet.getBloodPressure().isEmpty()) {
                bloodPresure.getItems().add(bracelet.getBloodPressure());
            }
        }
        bloodPresure.setEditable(false);
        bloodPresure.setCellFactory(TextFieldListCell.forListView());
    }


    private void generateBraceletAnalysis() {
        String analysis = braceletService.generateBraceletAnalysis();

        // Appliquer le style CSS au texte dans dataHealth
        dataHealth.setEditable(false); // Rendre la zone de texte non modifiable
        String style = "-fx-font-weight: bold; -fx-text-fill: #000000;";
        dataHealth.setStyle(style);

        // Définir le texte analysé dans dataHealth
        dataHealth.setText(analysis);
    }



    @FXML
    void makeappoitment(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddApoitment.fxml"));
        try {
            Parent root = loader.load();
            AddAppointmentController controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) boutonAppoitment.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    void generateRapport(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GenerateRapport.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) boutonGenerate.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




