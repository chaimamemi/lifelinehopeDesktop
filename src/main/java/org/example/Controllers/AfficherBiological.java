package org.example.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.models.BiologicalData;

import java.net.URL;
import java.util.ResourceBundle;

public class AfficherBiological implements Initializable {
    @FXML
    private TableView<BiologicalData> info;
    @FXML
    private TableColumn<BiologicalData, Integer> colid;
    @FXML
    private TableColumn<BiologicalData, String> colmesure;
    @FXML
    private TableColumn<BiologicalData, String> cl;
    @FXML
    private TableColumn<BiologicalData, String> colv;
    @FXML
    private TableColumn<BiologicalData, String> colp;
    @FXML
    private TableColumn<BiologicalData, Integer> colag;

    private Label LbBio;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


            colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            colmesure.setCellValueFactory(new PropertyValueFactory<>("measurementType"));
            cl.setCellValueFactory(new PropertyValueFactory<>("disease"));
            colv.setCellValueFactory(new PropertyValueFactory<>("value"));
            colp.setCellValueFactory(new PropertyValueFactory<>("patientName"));
            colag.setCellValueFactory(new PropertyValueFactory<>("patientAge"));

            AjoutBiological ajoutBiologicalController = new AjoutBiological();
            //ObservableList<BiologicalData> biologicalDataList = ajoutBiologicalController.getBiologicalData();


            //info.setItems(biologicalDataList);
        }

    }

