package org.example.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.Services.ServiceMedication;
import org.example.models.Medication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherMedication {

    @FXML
    private ListView<Medication> medicationListView;
    @FXML
    private TextField tfNameUpdate; // Champ de texte pour le nom de la médication à mettre à jour

    @FXML
    private TextField tfDosageUpdate;
    @FXML
    private Button btnupdate;

    @FXML
    private TextField serachField;
    @FXML
    private Button btndelete;

    @FXML
    private Button btntri;


    private List<Medication> allMedications;
    @FXML
    private TextField searchField;
    private ServiceMedication serviceMedication;
    @FXML
    private Button btnsearch;

    public void initialize() {
        serviceMedication = new ServiceMedication();
        allMedications = serviceMedication.getAll();
        refreshListView();
        btndelete.setOnAction(event -> deleteSelectedItems());
        btnupdate.setOnAction(event -> updateSelectedMedication());
        btnsearch.setOnAction(event -> searchMedications());
        btntri.setOnAction(event -> sortMedicationsByDosage());
        calculateAntibioticPercentages();


        medicationListView.setCellFactory(param -> new ListCell<Medication>() {
            @Override
            protected void updateItem(Medication item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Créer une représentation organisée des données pour la médication
                    String formattedData = String.format("Name: %s\n" +
                                    "Description: %s\n" +
                                    "Medical Note: %s\n" +
                                    "Dosage: %s",
                            item.getNameMedication(),
                            item.getDescription(),
                            item.getMedicalNote(),
                            item.getDosage());

                    setText(formattedData);
                }
            }
        });
    }


    public void setMedications(List<Medication> all) {
        allMedications = all;
        refreshListView();
    }

    @FXML
    private void deleteSelectedItems() {

        Medication selectedItem = medicationListView.getSelectionModel().getSelectedItem();


        if (selectedItem != null) {

            serviceMedication.delete(selectedItem);


            refreshListView();
        }
    }

    @FXML
    private void refreshListView() {
        List<Medication> medicationList = serviceMedication.getAll();
        medicationListView.setItems(FXCollections.observableArrayList(medicationList));
    }


    @FXML
    private void updateSelectedMedication() {

        Medication selectedItem = medicationListView.getSelectionModel().getSelectedItem();


        if (selectedItem != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateMedication.fxml"));
                Parent root = loader.load();


                UpdateMedicationController updateMedicationController = loader.getController();
                updateMedicationController.initData(selectedItem);


                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Medication");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void searchMedications() {
        // Récupérer le texte saisi dans le champ de recherche
        String searchText = searchField.getText().toLowerCase().trim();

        // Filtrer la liste des médications en fonction du texte saisi dans le champ de recherche
        List<Medication> filteredList = allMedications.stream()
                .filter(medication -> medication.getNameMedication().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        // Mettre à jour la ListView avec la liste filtrée
        medicationListView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void sortMedicationsByDosage() {
        // Tri par dosage
        medicationListView.setItems(FXCollections.observableArrayList(
                allMedications.stream()
                        .filter(medication -> !medication.getDosage().isEmpty()) // Filtrer les médications avec un dosage non vide
                        .sorted(Comparator.comparingDouble(medication -> {
                            // Extraire la partie numérique de la chaîne de dosage
                            String dosage = medication.getDosage();
                            String numericPart = dosage.replaceAll("[^0-9.]", "");
                            if (!numericPart.isEmpty()) {
                                return Double.parseDouble(numericPart);
                            } else {
                                return 0.0; // Retourner 0 si la partie numérique est vide
                            }
                        }))
                        .collect(Collectors.toList())
        ));

        // Affichage pour vérification
        System.out.println("Médications triées par dosage : " + medicationListView.getItems());
    }

    // Méthode pour calculer les pourcentages d'utilisation des antibiotiques
    @FXML
    private void calculateAntibioticPercentages() {
        // Supposons que nous avons une liste d'antibiotiques avec leurs utilisations
        Map<String, Integer> antibioticUsage = new HashMap<>();
        antibioticUsage.put("Amoxicillin", 25);
        antibioticUsage.put("Ciprofloxacin", 15);
        antibioticUsage.put("Doxycycline", 10);
        // Ajoutez d'autres antibiotiques et leurs utilisations ici

        // Calcul du nombre total d'utilisations d'antibiotiques
        int totalUsage = antibioticUsage.values().stream().mapToInt(Integer::intValue).sum();

        // Vérification si la liste d'antibiotiques n'est pas vide
        if (!antibioticUsage.isEmpty()) {
            // Construction de la chaîne de caractères pour l'affichage
            StringBuilder stringBuilder = new StringBuilder("Pourcentage d'utilisation des antibiotiques :\n");
            for (Map.Entry<String, Integer> entry : antibioticUsage.entrySet()) {
                String antibiotic = entry.getKey();
                int usage = entry.getValue();
                double percentage = (double) usage / totalUsage * 100;
                stringBuilder.append(antibiotic).append(": ").append(percentage).append("%\n");
            }

            // Affichage dans une fenêtre de dialogue (Alert)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pourcentage d'utilisation des antibiotiques");
            alert.setHeaderText(null);
            alert.setContentText(stringBuilder.toString());
            alert.showAndWait();
        } else {
            // Affichage d'un message si la liste d'antibiotiques est vide
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune donnée d'antibiotique");
            alert.setHeaderText(null);
            alert.setContentText("La liste des antibiotiques est vide.");
            alert.showAndWait();
        }
    }
}










