package org.example.Controllers.user;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.Services.ServiceMedication;
import org.example.models.Medication;

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


    @FXML
    private Button back;


    private List<Medication> allMedications;
    @FXML
    private TextField searchField;
    private ServiceMedication serviceMedication;
    @FXML
    private Button btnsearch;
    @FXML
    private ImageView qrCodeImageView;

    public void initialize() {
        serviceMedication = new ServiceMedication();
        allMedications = serviceMedication.getAll();
        refreshListView();
        btndelete.setOnAction(event -> deleteSelectedItems());
        btnupdate.setOnAction(event -> updateSelectedMedication());
        btnsearch.setOnAction(event -> searchMedications(searchField.getText().trim().toLowerCase()));
        btntri.setOnAction(event -> sortMedicationsByDosage());
        calculateAntibioticPercentages();
        back.setOnAction(this::navigateToAddMedication);
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
    private void searchMedications(String searchText) {
        // Filtrer la liste des médications en fonction du texte saisi dans le champ de recherche
        List<Medication> filteredList = allMedications.stream()
                .filter(medication -> medication.getNameMedication().toLowerCase().startsWith(searchText))
                .collect(Collectors.toList());

        // Mettre à jour la ListView avec la liste filtrée
        medicationListView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void sortMedicationsByDosage() {
        // Tri par dosage
        medicationListView.setItems(FXCollections.observableArrayList(
                allMedications.stream()
                        .filter(medication -> !medication.getDosage().isEmpty())
                        .sorted(Comparator.comparingDouble(medication -> {
                            String dosage = medication.getDosage();
                            String numericPart = dosage.replaceAll("[^0-9.]", "");
                            if (!numericPart.isEmpty()) {
                                return Double.parseDouble(numericPart);
                            } else {
                                return 0.0;
                            }
                        }))
                        .collect(Collectors.toList())
        ));
        System.out.println("Médications triées par dosage : " + medicationListView.getItems());
    }

    @FXML
    private void calculateAntibioticPercentages() {
        Map<String, Integer> antibioticUsage = new HashMap<>();
        antibioticUsage.put("Amoxicillin", 25);
        antibioticUsage.put("Ciprofloxacin", 15);
        antibioticUsage.put("Doxycycline", 10);
        int totalUsage = antibioticUsage.values().stream().mapToInt(Integer::intValue).sum();
        if (!antibioticUsage.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("Pourcentage d'utilisation des antibiotiques :\n");
            for (Map.Entry<String, Integer> entry : antibioticUsage.entrySet()) {
                String antibiotic = entry.getKey();
                int usage = entry.getValue();
                double percentage = (double) usage / totalUsage * 100;
                stringBuilder.append(antibiotic).append(": ").append(percentage).append("%\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pourcentage d'utilisation des antibiotiques");
            alert.setHeaderText(null);
            alert.setContentText(stringBuilder.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune donnée d'antibiotique");
            alert.setHeaderText(null);
            alert.setContentText("La liste des antibiotiques est vide.");
            alert.showAndWait();
        }
    }











    @FXML
    private void navigateToAddMedication(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMedication.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Médication");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
