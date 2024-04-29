import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import org.example.Controllers.Card;
import org.example.models.Medication;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Test2  implements Initializable {
    @FXML
    private HBox cardLayout;
    private List<Medication> recentlyAdded;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recentlyAdded = new ArrayList<>(recentlyAdded());
        try {
        for (int i = 0; i < recentlyAdded.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("card.fxml"));
            HBox cardBox = fxmlLoader.load();
            Card cardcontroller = fxmlLoader.getController();
            Card.setData(recentlyAdded.get(i));



        }

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private List<Medication> recentlyAdded() {


        List<Medication> medications = new ArrayList<>();
        Medication medication = new Medication();
        Medication.setName
    }
}

