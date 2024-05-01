package org.example.Controllers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.example.models.BiologicalData;

import java.io.IOException;
import java.util.List;

public class PDFGenerator {
    public static void generateBiologicalData(List<BiologicalData> dataList) {
        String dest = "biological_data_details.pdf";
        try {
            PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
            Document document = new Document(pdf, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont();

            // Couleurs personnalisées
            Color titleColor = new DeviceRgb(75, 156, 211);
            Color headerColor = new DeviceRgb(220, 230, 240); // Couleur de fond pour les titres de colonnes
            Color cellColor = new DeviceRgb(255, 255, 255); // Couleur de fond pour les cellules du tableau
            Color textColor = new DeviceRgb(33, 33, 33); // Couleur du texte dans le tableau

            // Titre
            Paragraph title = new Paragraph("Biological Data")
                    .setFont(font)
                    .setFontSize(36)
                    .setFontColor(titleColor)
                    .setBold()
                    .setItalic()
                    .setMarginBottom(20) // Ajoute une marge en bas du titre
                    .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER); // Centrer le titre horizontalement
            document.add(title);

            // Ajout de l'image en haut du document
            Image logo = new Image(ImageDataFactory.create("src/main/resources/img/logo.png"));
            document.add(logo);

            // Tableau pour afficher les données biologiques
            Table table = new Table(new float[]{2, 3, 3, 3, 2, 3, 4});
            // Appliquer la couleur de fond et de texte pour les cellules du tableau
            table.setBackgroundColor(cellColor);
            table.setFontColor(textColor);
            // Ajoutez les titres de colonnes avec une couleur de fond personnalisée et des bordures
            for (int i = 0; i < 7; i++) {
                table.addCell(new Paragraph(getHeaderTitle(i))
                        .setFont(font)
                        .setBold()
                        .setFontColor(textColor)
                        .setBackgroundColor(headerColor)
                        .setPadding(8) // Ajuste le padding pour les titres de colonnes
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                        .setBorder(new SolidBorder(1))); // Ajoute des bordures solides
            }
            // Ajoutez les données biologiques au tableau avec des bordures et des marges personnalisées
            for (BiologicalData data : dataList) {
                addCellWithBorder(table, data.getMeasurementType(), font, textColor);
                addCellWithBorder(table, data.getValue(), font, textColor);
                addCellWithBorder(table, data.getPatientName(), font, textColor);
                addCellWithBorder(table, data.getPatientLastName(), font, textColor);
                addCellWithBorder(table, String.valueOf(data.getPatientAge()), font, textColor);
                addCellWithBorder(table, data.getDisease(), font, textColor);
                addCellWithBorder(table, data.getOtherInformation(), font, textColor);
            }

            document.add(table);
            document.close();

            System.out.println("Le PDF des données biologiques a été généré avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du PDF des données biologiques : " + e.getMessage());
        }
    }

    // Méthode utilitaire pour obtenir le titre de la colonne
    private static String getHeaderTitle(int index) {
        switch (index) {
            case 0:
                return "Measurement Type";
            case 1:
                return "Value";
            case 2:
                return "Patient Name";
            case 3:
                return "Patient Last Name";
            case 4:
                return "Age";
            case 5:
                return "Disease";
            case 6:
                return "Other Information";
            default:
                return "";
        }
    }

    // Méthode utilitaire pour ajouter une cellule avec des bordures
    private static void addCellWithBorder(Table table, String content, PdfFont font, Color textColor) {
        table.addCell(new Paragraph(content)
                .setFont(font)
                .setFontColor(textColor)
                .setPadding(8) // Ajuste le padding pour le contenu des cellules
                .setBorder(new SolidBorder(1)) // Ajoute des bordures solides
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)); // Centrer le texte horizontalement
    }
}
