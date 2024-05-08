package org.example.Controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import org.example.Controllers.PDFGenerator;
import org.example.Controllers.user.AfficherBiological;
import org.example.Controllers.user.AfficherMedication;
import org.example.Services.ServiceMedication;
import org.example.models.BiologicalData;
import org.example.models.Medication;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.Main.filterMedications;

public class Dashboard implements Initializable {
    @FXML
    private HBox cardLayout;
    @FXML
    private TextField searchm;

    @FXML
    private ListView<Medication> listm;
    @FXML
    private Button btnstat;
    @FXML
    private Button show ;

    private AfficherMedication afficherMedication;
    private AfficherBiological afficherBiological = new AfficherBiological();
    @FXML
    private Button btnm;

    @FXML
    private Button btnpdf;

    private ServiceMedication serviceMedication;
    private List<Medication> allMedications;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnstat.setOnAction(event -> showStatistics());
        btnpdf.setOnAction(event -> generatePDF());

        serviceMedication = new ServiceMedication();
        allMedications = serviceMedication.getAll();
        refreshListView();
        searchm.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMedications(newValue);
        });

        // Définition d'un cell factory pour personnaliser l'affichage des éléments
        listm.setCellFactory(lv -> new ListCell<Medication>() {
            @Override
            protected void updateItem(Medication medication, boolean empty) {
                super.updateItem(medication, empty);
                if (medication == null || empty) {
                    setText(null);
                } else {
                    // Affichage personnalisé avec espaces et mise en forme des titres
                    String formattedText = "Name Medication: " + medication.getNameMedication() +
                            "\nDescription: " + medication.getDescription() +
                            "\nMedical Note: " + medication.getMedicalNote() +
                            "\nDosage: " + medication.getDosage();

                    setText(formattedText);

                    // Appliquer le style en gras et en noir pour les titres
                    setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

                    // Appliquer le style rouge uniquement aux valeurs récupérées par les getters spécifiques
                    if (getText().contains("Name Medication:") ||
                            getText().contains("Description:") ||
                            getText().contains("Medical Note:") ||
                            getText().contains("Dosage:")) {
                        // Appliquer le style rouge pour les commentaires
                        setStyle("-fx-text-fill: red;");
                    }
                }
            }
        });
    }






    private void filterMedications(String searchText) {
        // Créez une FilteredList pour filtrer les médications en fonction de la saisie de l'utilisateur
        FilteredList<Medication> filteredList = new FilteredList<>(FXCollections.observableArrayList(allMedications));

        // Définissez le prédicat de filtrage pour rechercher le texte saisi dans le nom de la médication
        filteredList.setPredicate(medication -> {
            // Si le champ de recherche est vide, affichez toutes les médications
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            // Recherche insensible à la casse
            return medication.getNameMedication().toLowerCase().contains(searchText.toLowerCase());
        });

        // Créez une SortedList à partir de la FilteredList pour trier les résultats
        SortedList<Medication> sortedList = new SortedList<>(filteredList);

        // Liez la SortedList à la ListView pour afficher les résultats filtrés et triés
        listm.setItems(sortedList);
    }

    // ...


    @FXML
    private void refreshListView() {
        List<Medication> medicationList = serviceMedication.getAll();
        listm.setItems(FXCollections.observableArrayList(medicationList));
    }



    public void setMedications(List<Medication> all) {
        allMedications = all;
        refreshListView();
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

    @FXML
    private void listBiologicalData(ActionEvent actionEvent) {
        // Appeler la méthode listBiological() de l'instance afficherBiological
        afficherBiological.listBiological(actionEvent);
    }

}

