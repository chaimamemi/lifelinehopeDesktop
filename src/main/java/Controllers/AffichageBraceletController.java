package Controllers;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.application.Platform;
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
import javafx.event.ActionEvent; // Correction de l'importation

import java.io.IOException;

public class AffichageBraceletController {

    @FXML
    private TextField bloodPressure;

    @FXML
    private TextField gps;

    @FXML
    private TextField heartRate;

    @FXML
    private TextField movement;

    @FXML
    private TextField temperature;

    @FXML
    private VBox address;

    @FXML
    private Button boutonAffiche;

    private MapView mapView;
    private MapPoint eiffelPoint = null; // Initialiser à null

    private BraceletService braceletService = new BraceletService();

    @FXML
    public void initialize() {
        boutonAffiche.setOnAction(this::handleDataBraceletHistory);
        createMapView();
        address.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        displayLastBracelet(); // Appeler displayLastBracelet après la création de la MapView
    }

    private void createMapView() {
        mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.setZoom(5);
        mapView.addLayer(new CustomMapLayer(eiffelPoint));
    }

    private static class CustomMapLayer extends MapLayer {

        private final Node marker;
        private final MapPoint eiffelPoint;

        public CustomMapLayer(MapPoint eiffelPoint) {
            this.eiffelPoint = eiffelPoint;
            marker = new Circle(5, Color.RED);
            getChildren().add(marker);
        }

        public void setMarkerPosition(MapPoint point) {
            Point2D point2D = getMapPoint(eiffelPoint.getLatitude(), eiffelPoint.getLongitude());
            marker.setTranslateX(point2D.getX());
            marker.setTranslateY(point2D.getY());
        }
    }

    private void displayLastBracelet() {
        Bracelet lastBracelet = braceletService.getLastBracelet();

        if (lastBracelet != null) {
            // Mettre à jour les coordonnées de eiffelPoint avec celles du dernier bracelet
            Double latitude = lastBracelet.getLatitude();
            Double longitude = lastBracelet.getLongitude();
            if (latitude != null && longitude != null) {
                eiffelPoint = new MapPoint(latitude, longitude);
                Platform.runLater(() -> {
                    bloodPressure.setText(lastBracelet.getBloodPressure() != null ? lastBracelet.getBloodPressure() : "");
                    temperature.setText(lastBracelet.getTemperature() != null ? lastBracelet.getTemperature().toString() : "");
                    heartRate.setText(lastBracelet.getHeartRate() != null ? lastBracelet.getHeartRate().toString() : "");
                    movement.setText(lastBracelet.getMovement() != null ? lastBracelet.getMovement() : "");
                    gps.setText(lastBracelet.getGps() != null ? lastBracelet.getGps() : "");

                    bloodPressure.setEditable(false);
                    temperature.setEditable(false);
                    heartRate.setEditable(false);
                    movement.setEditable(false);
                    gps.setEditable(false);
                });
            }
        }
    }

    @FXML
    void handleDataBraceletHistory(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheAllBraceletData.fxml"));
        try {
            Parent root = loader.load();
            AfficheAllBraceletController controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) boutonAffiche.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
