package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Test {
    public void AjouterMedication(ActionEvent actionEvent) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMedication.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter Médication");
                stage.show();
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
                e.printStackTrace();
            } catch (Exception ex) {
                System.err.println("Erreur inattendue : " + ex.getMessage());
                ex.printStackTrace();
            }
        }

    public void AjouterBiological(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutBiological.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Médication");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Erreur inattendue : " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}

