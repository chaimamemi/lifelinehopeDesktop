package org.example.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Services.ServiceMedication;
import org.example.models.Medication;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Statistics  implements Initializable {
    @FXML
    private PieChart piechart;
    @FXML
    private  Button goback;
    private final ServiceMedication serviceMedication;

    public Statistics() {
        this.serviceMedication = new ServiceMedication();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populatePieChart();
        goback.setOnAction(event -> navigateToDashboard());
    }

    private void populatePieChart() {
        List<Medication> medicationList = serviceMedication.getAll();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Créer une carte pour stocker la longueur de la description de chaque médicament
        Map<String, Integer> medicationDescriptionLengthMap = new HashMap<>();

        // Calculer la longueur totale de toutes les descriptions de médicaments
        int totalLength = 0;
        for (Medication medication : medicationList) {
            String description = medication.getDescription();
            int length = description.length();
            totalLength += length;

            // Ajouter ou mettre à jour la longueur de la description dans la carte
            medicationDescriptionLengthMap.put(medication.getNameMedication(), length);
        }

        // Trier la carte par longueur de description dans l'ordre décroissant
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(medicationDescriptionLengthMap.entrySet());
        sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Ajouter les données pour les cinq premiers médicaments les plus fréquents
        int otherLength = 0;
        for (int i = 0; i < Math.min(5, sortedEntries.size()); i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            String label = entry.getKey();
            double value = (double) entry.getValue() / totalLength;
            pieChartData.add(new PieChart.Data(label, value));
            otherLength += entry.getValue(); // Ajouter à la longueur totale des "autres"
        }

        // Si le nombre total de médicaments est supérieur à cinq, ajouter une catégorie "Autres"
        if (sortedEntries.size() > 5) {
            String label = "others";
            double value = (double) (totalLength - otherLength) / totalLength;
            pieChartData.add(new PieChart.Data(label, value));
        }

        // Afficher les données dans le PieChart
        piechart.setData(pieChartData);
    }






    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml")); // Changer le chemin du fichier FXML selon le besoin
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) goback.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
