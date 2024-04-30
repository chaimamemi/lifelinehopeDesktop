package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import org.example.Services.ServiceMedication;

import java.net.URL;
import java.util.ResourceBundle;

public class Statistics  implements Initializable {
    @FXML
    private PieChart piechart;
    private final ServiceMedication serviceMedication;

    public Statistics() {
        this.serviceMedication=new ServiceMedication();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populatePieChart();

    }

    private void populatePieChart() {
    }
}
