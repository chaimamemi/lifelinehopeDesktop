package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.example.models.Medication;

import javax.swing.text.html.ImageView;

public class Card {
    @FXML
    private HBox idhbox;
    @FXML
    private TextField ftNameMedicaiton;
    @FXML
    private TextField ftdes;
    @FXML
    private TextField ftNote;
    @FXML
    private TextField ftdos;
    @FXML
    private ImageView imgmed;



    public static void setData(Medication medication){

        ftNameMedicaiton.setText(Medication.getNameMedication());
        ftdes.setText(Medication.getDescription());
        ftdos.setText(Medication.getDosage());

    }
}
