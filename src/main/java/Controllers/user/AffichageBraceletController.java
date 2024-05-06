package Controllers.user;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.Bracelet;
import services.BraceletService;

import java.io.IOException;

public class AffichageBraceletController {

    @FXML
    private TextField bloodPressure, gps, heartRate, movement, temperature;
    @FXML
    private VBox address;
    @FXML
    private Button boutonAffiche;

    @FXML
    private Button chat;

    private MapView mapView;
    private MapPoint currentLocation = null;
    private CustomMapLayer customMapLayer;
    private BraceletService braceletService = new BraceletService();

    @FXML
    public void initialize() {
        createMapView();
        address.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        displayLastBracelet();
        boutonAffiche.setOnAction(this::handleDataBraceletHistory);
        chat.setOnAction(this::goaskchat);
    }

    private void createMapView() {
        mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.setZoom(5);
        customMapLayer = new CustomMapLayer();
        mapView.addLayer(customMapLayer);

        // Setting the initial center of the map to fixed coordinates
        MapPoint initialCenter = new MapPoint(36.8002068, 10.1857757);
        mapView.setCenter(initialCenter);


    }



        class CustomMapLayer extends MapLayer {
        private Node marker = new Circle(5, Color.RED);

        public CustomMapLayer() {
            getChildren().add(marker);
            marker.setVisible(false);
        }

        public void updateMarker(MapPoint point) {
            if (point != null) {
                Point2D mapPoint = baseMap.getMapPoint(point.getLatitude(), point.getLongitude());
                Platform.runLater(() -> {
                    marker.setTranslateX(mapPoint.getX());
                    marker.setTranslateY(mapPoint.getY());
                    marker.setVisible(true);
                });
            }
        }
    }

    private void displayLastBracelet() {
        Bracelet lastBracelet = braceletService.getLastBracelet();
        if (lastBracelet != null && lastBracelet.getLatitude() != null && lastBracelet.getLongitude() != null) {
            currentLocation = new MapPoint(lastBracelet.getLatitude(), lastBracelet.getLongitude());
            customMapLayer.updateMarker(currentLocation);
            Platform.runLater(() -> {
                updateTextFields(lastBracelet);
            });
        }
    }

    private void updateTextFields(Bracelet bracelet) {
        bloodPressure.setText(bracelet.getBloodPressure());
        temperature.setText(String.valueOf(bracelet.getTemperature()));
        heartRate.setText(String.valueOf(bracelet.getHeartRate()));
        movement.setText(bracelet.getMovement());
        gps.setText(bracelet.getGps());

        // Set editable to false
        bloodPressure.setEditable(false);
        temperature.setEditable(false);
        heartRate.setEditable(false);
        movement.setEditable(false);
        gps.setEditable(false);
    }

    @FXML
    void handleDataBraceletHistory(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/afficheAllBraceletData.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) boutonAffiche.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goaskchat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatGpt.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) chat.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
