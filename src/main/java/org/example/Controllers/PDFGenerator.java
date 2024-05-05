package org.example.Controllers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.example.models.BiologicalData;

import java.io.IOException;
import java.util.List;

public class PDFGenerator {
    public static void generateBiologicalData(List<BiologicalData> dataList) {
        String dest = "biological_data_details.pdf";
        try {
            PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
            Document document = new Document(pdf, PageSize.A4);

            // Couleurs personnalisées
            Color titleColor = new DeviceRgb(52, 152, 219); // Bleu vif pour le titre
            Color textColor = new DeviceRgb(33, 33, 33); // Noir pour le texte

            // Polices de caractères
            PdfFont titleFont = PdfFontFactory.createFont();
            PdfFont contentFont = PdfFontFactory.createFont();

            // Logo à gauche du titre
            Image logoImage = new Image(ImageDataFactory.create("src/main/resources/img/logo.png"));
            logoImage.setWidth(30).setHeight(30); // Taille réduite du logo
            Paragraph title = new Paragraph()
                    .add(logoImage)
                    .add("Biological Data")
                    .setFont(titleFont)
                    .setFontSize(36)
                    .setFontColor(titleColor)
                    .setBold()
                    .setItalic()
                    .setMarginBottom(20) // Ajoute une marge en bas du titre
                    .setTextAlignment(TextAlignment.CENTER); // Centrer le titre horizontalement

            document.add(title);

            // Tableau pour afficher les données biologiques
            Table table = new Table(new float[]{3, 3, 3, 3, 3, 3, 3});
            table.setWidth(UnitValue.createPercentValue(100));
            // Appliquer la couleur de fond bleu clair pour les cellules du tableau
            table.setBackgroundColor(new DeviceRgb(173, 216, 230)); // Bleu clair pour le tableau

            // Modification des couleurs et de la police pour les titres de colonnes
            for (int i = 0; i < 7; i++) {
                table.addCell(new Paragraph(getHeaderTitle(i))
                        .setFont(contentFont)
                        .setBold()
                        .setFontColor(textColor)
                        .setPadding(8) // Ajuste le padding pour les titres de colonnes
                        .setTextAlignment(TextAlignment.CENTER));
            }

            // Ajoutez les données biologiques au tableau avec des bordures et des marges personnalisées
            for (BiologicalData data : dataList) {
                addCell(table, data.getMeasurementType(), contentFont, textColor);
                addCell(table, data.getValue(), contentFont, textColor);
                addCell(table, data.getPatientName(), contentFont, textColor);
                addCell(table, data.getPatientLastName(), contentFont, textColor);
                addCell(table, String.valueOf(data.getPatientAge()), contentFont, textColor);
                addCell(table, data.getDisease(), contentFont, textColor);
                addCell(table, data.getOtherInformation(), contentFont, textColor);
            }

            document.add(table);

            // Ajout de l'image "doct.png" à droite de la page
            Image doctorImage = new Image(ImageDataFactory.create("src/main/resources/img/doct.png"));
            doctorImage.setWidth(100).setHeight(100); // Taille réduite de l'image
            document.add(doctorImage);

            // Ajout du message "Thank you for downloading AjoutBiological History" en gras
            Paragraph thankYouMessage = new Paragraph("Thank you for downloading AjoutBiological History")
                    .setFont(contentFont)
                    .setFontColor(textColor)
                    .setBold() // Mettre le texte en gras
                    .setMarginTop(50) // Marge en haut du message
                    .setTextAlignment(TextAlignment.CENTER); // Centrer le message horizontalement
            document.add(thankYouMessage);

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

    // Méthode utilitaire pour ajouter une cellule avec des bordures au tableau
    private static void addCell(Table table, String content, PdfFont font, Color textColor) {
        table.addCell(new Paragraph(content)
                .setFont(font)
                .setFontColor(textColor)
                .setPadding(8) // Ajuste le padding pour le contenu des cellules
                .setTextAlignment(TextAlignment.CENTER));
    }
}
