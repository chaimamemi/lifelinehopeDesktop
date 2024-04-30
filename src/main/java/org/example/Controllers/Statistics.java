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
import java.util.List;
import java.util.ResourceBundle;

public class Statistics  implements Initializable {
    @FXML
    private PieChart piechart;
    private final ServiceMedication serviceMedication;

    public Statistics() {
        this.serviceMedication = new ServiceMedication();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populatePieChart();
    }

    private void populatePieChart() {
        List<Medication> medicationList = serviceMedication.getAll();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Collecting data for the pie chart
        for (Medication medication : medicationList) {
            pieChartData.add(new PieChart.Data(medication.getNameMedication(), medication.getDescription().length()));
        }

        piechart.setData(pieChartData);
    }

    @FXML
    private Button goback;



}
