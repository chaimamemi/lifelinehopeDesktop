import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ChartExample extends Application {

    @Override
    public void start(Stage stage) {
        // Créer les données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Grippe", 1));
        series.getData().add(new XYChart.Data<>("Rhume", 2));
        series.getData().add(new XYChart.Data<>("Fièvre", 3));
        series.getData().add(new XYChart.Data<>("Toux", 4));
        series.getData().add(new XYChart.Data<>("Migraine", 5));

        // Créer l'axe des catégories
        CategoryAxis xAxis = new CategoryAxis();

        // Créer l'axe des valeurs
        NumberAxis yAxis = new NumberAxis();

        // Créer le graphique à barres
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);

        // Créer la scène et l'ajouter à la scène principale
        Scene scene = new Scene(barChart, 800, 600);

        // Définir le titre de la fenêtre
        stage.setTitle("Exemple de graphique à barres avec JavaFX");

        // Ajouter la scène à la fenêtre et l'afficher
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
