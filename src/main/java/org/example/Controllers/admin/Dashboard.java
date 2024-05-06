package org.example.Controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import org.example.Controllers.PDFGenerator;
import org.example.Controllers.user.AfficherMedication;
import org.example.Services.ServiceMedication;
import org.example.models.BiologicalData;
import org.example.models.Medication;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    @FXML
    private HBox cardLayout;
    @FXML
    private Button btnstat;
    @FXML
    private Button show ;

    private AfficherMedication afficherMedication;
    @FXML
    private Button btnm;

    @FXML
    private Button btnpdf;
    @FXML
    private List<Medication> listm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnstat.setOnAction(event -> showStatistics());
        btnpdf.setOnAction(event -> generatePDF());
        btnm.setOnAction(event->fetchdata());

    }

    @FXML
    private void showStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistics.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Medication Statistics");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateBiologicalData(ActionEvent actionEvent) {
    }

    @FXML
    private void generatePDF() {

        List<BiologicalData> dataList = fetchData(); // Méthode à implémenter pour récupérer les données

        // Appel de la méthode de génération de PDF avec les données récupérées
        PDFGenerator.generateBiologicalData(dataList);
    }

    private List<BiologicalData> fetchData() {

        return List.of();
    }


    @FXML
    private void listMedication(ActionEvent actionEvent) {
        // Vérifiez d'abord si afficherMedication est null
        if (afficherMedication == null) {
            // Créez une nouvelle instance de AfficherMedication
            afficherMedication = new AfficherMedication();
        }

        // Obtenez la liste des médicaments à partir de la méthode fetchData() déjà définie
        List<Medication> medicationList = fetchdata();

        // Appelez la méthode setMedications() sur l'instance afficherMedication pour définir les médicaments
        afficherMedication.setMedications(medicationList);
    }
    @FXML
    private List<Medication> fetchdata() {
        // Créez une instance du service de médicaments
        ServiceMedication serviceMedication = new ServiceMedication();

        // Utilisez le service pour récupérer la liste des médicaments
        listm = serviceMedication.getAll();

        // Retournez la liste des médicaments
        return listm;
    }
    @FXML
    private void showMostUsedMedicationDetails(ActionEvent actionEvent) {
        // Récupérer le médicament le plus utilisé
        Medication mostUsedMedication = getMostUsedMedication();

        // Vérifier si un médicament a été trouvé
        if (mostUsedMedication != null) {
            // Afficher les détails du médicament le plus utilisé
            System.out.println("Médicament le plus utilisé : " + mostUsedMedication.getNameMedication());
            System.out.println("Description : " + mostUsedMedication.getDescription());
            System.out.println("Autres détails...");
        } else {
            // Afficher un message si aucun médicament n'a été trouvé
            System.out.println("Aucun médicament trouvé.");
        }
    }

    // Méthode pour obtenir le médicament le plus utilisé
    private Medication getMostUsedMedication() {
        // Logique pour déterminer le médicament le plus utilisé
        // Vous pouvez utiliser votre propre logique basée sur les données de votre liste de médicaments
        // Par exemple, compter le nombre d'occurrences de chaque médicament et trouver celui avec le nombre le plus élevé
        // Retourner le médicament le plus utilisé
        return null; // Remplacer null par le médicament réel trouvé
    }

}

