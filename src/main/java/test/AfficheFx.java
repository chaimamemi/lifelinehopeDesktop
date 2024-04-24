package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficheFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheAppointement.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root );

            primaryStage.setTitle("Gestion Apoitment");
            primaryStage.setScene(scene);
            primaryStage.show();



        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}

