package org.example.Controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import org.example.models.BiologicalData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PDFGenerator{

    public void generatePDF(BiologicalData data) {
        String FILE = "String FILE = \"C:/Users/USER/IdeaProjects/PIDEvv/pdf/biological_data.pdf\";\n";
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(FILE));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Ajouter les métadonnées
            addMetaData(document);

            // Ajouter la page de titre
            addTitlePage(document);

            // Ajouter le contenu du PDF
            addContent(document, data);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ajouter les métadonnées au PDF
    private void addMetaData(Document document) {
    }

    // Ajouter la page de titre au PDF
    private void addTitlePage(Document document) throws IOException {
        Paragraph title = new Paragraph("Biological Data");
        title.setFontSize(24).setFontColor(new DeviceRgb(0, 0, 255)).setBold();
        document.add(title);

        Paragraph subtitle = new Paragraph("Generated on: " + new Date().toString());
        subtitle.setFontSize(12);
        document.add(subtitle);

        document.add(new Paragraph("\n\n")); // Ajouter un espace vertical
    }

    // Ajouter le contenu du PDF
    // Ajouter le contenu du PDF
    private void addContent(Document document, BiologicalData data) throws IOException {
        Paragraph title = new Paragraph("Biological Data Details");
        title.setFontSize(18).setBold().setUnderline();
        document.add(title);

        document.add(new Paragraph("\n")); // Ajouter un espace vertical

        // Créer un tableau pour afficher les détails des données biologiques
        Table table = new Table(new float[]{2, 5}); // 2 colonnes avec des largeurs proportionnelles

        // Ajouter les détails des données biologiques dans le tableau
        addTableRow(table, "Measurement Type:", data.getMeasurementType());
        addTableRow(table, "Value:", data.getValue());
        addTableRow(table, "Patient Name:", data.getPatientName());
        addTableRow(table, "Patient Last Name:", data.getPatientLastName());
        addTableRow(table, "Patient Age:", String.valueOf(data.getPatientAge()));
        addTableRow(table, "Disease:", data.getDisease());
        addTableRow(table, "Other Information:", data.getOtherInformation());

        document.add(table);
    }

    // Méthode utilitaire pour ajouter une ligne au tableau avec une clé et une valeur
    private void addTableRow(Table table, String key, String value) {
        table.addCell(new Paragraph(key));
        table.addCell(new Paragraph(value));
    }

}
