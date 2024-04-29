package com.example.sinda;

import com.example.sinda.controllers.admin.AutomatiqueAlert;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {
    private AutomatiqueAlert automatiqueAlert =new AutomatiqueAlert();
    @Override
    public void start(Stage stage) throws IOException {

      FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/user/Listealert.fxml"));
      //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/admin/Alerts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
      ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> automatiqueAlert.CreatAlaert();;

        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 0, 3, TimeUnit.MINUTES);



    }

    public static void main(String[] args) {
        launch();
    }
}