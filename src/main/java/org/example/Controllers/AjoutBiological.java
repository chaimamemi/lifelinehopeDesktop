package org.example.Controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Services.ServiceBiologicalData;
import org.example.models.BiologicalData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AjoutBiological {
    ServiceBiologicalData sp = new ServiceBiologicalData();
    PDFGenerator pdfGenerator = new PDFGenerator();

    @FXML
    Button btnadd;

    @FXML
    private TextField Ftm;
    @FXML
    private TextField Ftv;
    @FXML
    private TextField Ftn;

    @FXML
    private TextField Ftla;
    @FXML
    private TextField fta;
    @FXML
    private TextField FtD;
    @FXML
    private TextField FtOth;

    @FXML
    private Button btnshow;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnadd.setOnAction(this::ajouterBiological);
        btnshow.setOnAction(this::afficherBiological);
    }

    @FXML
    void afficherBiological(ActionEvent event) {


        List<BiologicalData> biologicalList = sp.getAll();

        // Générer le PDF avec la liste complète des données biologiques
        pdfGenerator.generateBiologicalData(biologicalList);

        // Afficher un message de confirmation
        showAlert("Succès", "Le PDF des données biologiques a été généré avec succès !");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherBiological.fxml"));
        try {
            Parent root = loader.load();
            AfficherBiological controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue AfficherBiological.");
        }
    }

    @FXML
    void ajouterBiological(ActionEvent event) {
        if (areFieldsEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs.");
        } else {
            BiologicalData data = new BiologicalData();
            data.setMeasurementType(Ftm.getText());
            data.setValue(Ftv.getText());
            data.setPatientName(Ftn.getText());
            data.setPatientLastName(Ftla.getText());
            // Ajout de l'âge sans vérification préalable
            try {
                int age = Integer.parseInt(fta.getText());
                data.setPatientAge(age);
            } catch (NumberFormatException e) {
                showAlert("Erreur de saisie", "Veuillez saisir un nombre valide pour l'âge.");
                return; // Sortir de la méthode si l'âge n'est pas valide
            }

            data.setDisease(FtD.getText());
            data.setOtherInformation(FtOth.getText());
            sp.add(data);
            pdfGenerator.generateBiologicalData(List.of(data));
        }
    }

    private void generatePDF(BiologicalData data) {
        String projectDirectory = System.getProperty("user.dir");
        String filePath = projectDirectory + File.separator + "pdf" + File.separator + "biological_data.pdf";

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            com.itextpdf.kernel.pdf.PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

            // Ajouter les métadonnées
            addMetaData(document);

            // Ajouter la page de titre
            addTitlePage(document);

            // Ajouter le contenu du PDF
            addContent(document, (List<BiologicalData>) data);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ajouter les métadonnées au PDF
    private void addMetaData(com.itextpdf.layout.Document document) {
        // Ajouter les métadonnées ici si nécessaire
    }

    // Ajouter la page de titre au PDF
    private void addTitlePage(com.itextpdf.layout.Document document) {
        Paragraph title = new Paragraph("Biological Data");
        title.setFontSize(24);
        document.add(title);

        Paragraph subtitle = new Paragraph("Generated on: " + new java.util.Date().toString());
        subtitle.setFontSize(12);
        document.add(subtitle);

        document.add(new Paragraph("\n\n")); // Ajouter un espace vertical
    }

    // Ajouter le contenu du PDF
    private void addContent(Document document, List<BiologicalData> dataList) {
        Paragraph title = new Paragraph("Biological Data Details");
        title.setFontSize(18);
        document.add(title);

        document.add(new Paragraph("\n")); // Ajouter un espace vertical

        // Créer un tableau pour afficher les détails des données biologiques
        Table table = new Table(new float[]{2, 5}); // 2 colonnes avec des largeurs proportionnelles

        // Ajouter les détails des données biologiques dans le tableau
        for (BiologicalData data : dataList) {
            addTableRow(table, "Measurement Type:", data.getMeasurementType());
            addTableRow(table, "Value:", data.getValue());
            addTableRow(table, "Patient Name:", data.getPatientName());
            addTableRow(table, "Patient Last Name:", data.getPatientLastName());
            addTableRow(table, "Patient Age:", String.valueOf(data.getPatientAge()));
            addTableRow(table, "Disease:", data.getDisease());
            addTableRow(table, "Other Information:", data.getOtherInformation());

            document.add(table);
        }
    }

    // Méthode utilitaire pour ajouter une ligne au tableau avec une clé et une valeur
    private void addTableRow(Table table, String key, String value) {
        table.addCell(key);
        table.addCell(value);
    }




    private boolean areFieldsEmpty() {

        return Ftm.getText().isEmpty() || Ftv.getText().isEmpty() || Ftn.getText().isEmpty() || FtD.getText().isEmpty() || FtOth.getText().isEmpty() || Ftla.getText().isEmpty();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





}







