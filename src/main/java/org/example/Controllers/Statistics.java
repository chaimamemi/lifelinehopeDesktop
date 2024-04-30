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
        this.serviceMedication=new ServiceMedication();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populatePieChart();

    }

    private void populatePieChart() {

        List<Medication> topRatedAvisList = serviceMedication.getTopRatedAvis();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Medication medication : topRatedAvisList) {
            pieChartData.add(new PieChart.Data(Medication. getMedicalNote(), Double.parseDouble(medication.get())));
        }
        piechart.setData(pieChartData);
    }
    @FXML
    private Button goback;

    @FXML
    void gobackon(ActionEvent event) {
        navigateToDashboard();
    }
    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Test2.fxml"));
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
