package org.example.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.Services.ServiceBiologicalData;
import org.example.models.BiologicalData;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherBiological {
    @FXML
    private ListView<BiologicalData> listView;

    @FXML
    private Button btndel;
    @FXML
    private TextField searchField;

    @FXML
    private Button btnupd;
    @FXML
    private Button btnsearch;
    private ServiceBiologicalData serviceBiologicalData;

    @FXML
    private Button FTaverage;

    public void initialize() {
        serviceBiologicalData = new ServiceBiologicalData();
        refreshListView();
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(BiologicalData item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Créer une représentation organisée des données
                    String formattedData = String.format("Measurement Type: %s\n" +
                                    "Value: %s\n" +
                                    "Patient Name: %s\n" +
                                    "Patient Last Name: %s\n" +
                                    "Age: %d\n" +
                                    "Disease: %s\n" +
                                    "Others: %s",
                            item.getMeasurementType(),
                            item.getValue(),
                            item.getPatientName(),
                            item.getPatientLastName(),
                            item.getPatientAge(),
                            item.getDisease(),
                            item.getOtherInformation());

                    setText(formattedData);
                }
            }
        });

        // Ajouter un gestionnaire d'événements pour le bouton "Delete"
        btndel.setOnAction(event -> deleteSelectedItems());
        btnupd.setOnAction(event -> updateSelectedItem());
        btnsearch.setOnAction(event -> searchBiologicalData());
        FTaverage.setOnAction(event -> CalculAverage());
        checkForSurveillance();
    }


    private void refreshListView() {
        List<BiologicalData> dataList = serviceBiologicalData.getAll();

        // Tri des données par âge en utilisant un comparateur personnalisé
        dataList.sort(Comparator.comparingInt(BiologicalData::getPatientAge));

        listView.setItems(FXCollections.observableArrayList(dataList));
    }

    private void deleteSelectedItems() {
        // Récupérer l'élément sélectionné dans la ListView
        BiologicalData selectedItem = listView.getSelectionModel().getSelectedItem();

        // Vérifier si un élément est sélectionné
        if (selectedItem != null) {
            // Supprimer l'élément de la base de données
            serviceBiologicalData.delete(selectedItem);

            // Rafraîchir la ListView pour mettre à jour l'affichage
            refreshListView();
        }
    }




    @FXML
    private void updateSelectedItem() {

        BiologicalData selectedItem = listView.getSelectionModel().getSelectedItem();


        if (selectedItem != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateBiological.fxml"));
                AnchorPane updateForm = loader.load();


                UpdateBiological controller = loader.getController();
                controller.initData(selectedItem);


                Stage updateStage = new Stage();
                Scene scene = new Scene(updateForm);
                updateStage.setScene(scene);
                updateStage.show();
                PDFGenerator.generateBiologicalData(selectedItem);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }


    @FXML
    private void searchBiologicalData() {

        String searchText = searchField.getText().toLowerCase().trim();


        List<BiologicalData> allData = serviceBiologicalData.getAll();


        List<BiologicalData> filteredData = allData.stream()
                .filter(data ->
                        data.getPatientLastName().toLowerCase().contains(searchText) ||
                                data.getPatientName().toLowerCase().contains(searchText) ||

                                String.valueOf(data.getPatientAge()).toLowerCase().contains(searchText))
                .collect(Collectors.toList());


        listView.setItems(FXCollections.observableArrayList(filteredData));
    }

    @FXML
    private void CalculAverage() {
        List<BiologicalData> dataList = serviceBiologicalData.getAll();


        List<Integer> ages = dataList.stream()
                .map(BiologicalData::getPatientAge)
                .collect(Collectors.toList());


        double moyenne = ages.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0);


        System.out.println("Moyenne d'âge des patients : " + moyenne);
    }

    @FXML
    private void checkForSurveillance() {
        List<BiologicalData> dataList = serviceBiologicalData.getAll();


        boolean surveillanceNecessaire = dataList.stream()
                .anyMatch(data -> "cardiaque".equalsIgnoreCase(data.getDisease()) || "cancer".equalsIgnoreCase(data.getDisease()));


        if (surveillanceNecessaire) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Surveillance obligatoire");
            alert.setHeaderText(null);
            alert.setContentText("Une surveillance est obligatoire pour les patients atteints de maladies cardiaques ou de cancer.");
            alert.showAndWait();
        }

    }


}

