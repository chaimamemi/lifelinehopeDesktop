package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Services.ServiceMedication;
import org.example.models.Medication;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherMedication implements Initializable {

    @FXML
    private TableView<Medication> tableView;

    @FXML
    private TableColumn<Medication, Integer> idColumn;

    @FXML
    private TableColumn<Medication, String> nameColumn;

    @FXML
    private TableColumn<Medication, String> dosageColumn;

    @FXML
    private TableColumn<Medication, String> descColumn;

    @FXML
    private TableColumn<Medication, String> noteColumn;

    private ServiceMedication serviceMedication;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceMedication = new ServiceMedication();
        initTableView();
        loadMedications();
    }

    private void initTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameMedication"));
        dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("medicalNote"));
    }

    public void loadMedications() {
        tableView.getItems().clear();
        List<Medication> medications = serviceMedication.getAll();
        tableView.getItems().addAll(medications);
    }

    public void setLbmedication(String string) {
    }
}
