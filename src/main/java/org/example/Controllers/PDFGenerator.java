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

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
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
            Color headerColor = new DeviceRgb(189, 195, 199); // Gris pour les titres de colonnes
            Color cellColor = new DeviceRgb(243, 243, 243); // Gris clair pour les cellules du tableau
            Color textColor = new DeviceRgb(33, 33, 33); // Noir pour le texte

            // Polices de caractères
            PdfFont titleFont = PdfFontFactory.createFont();
            PdfFont contentFont = PdfFontFactory.createFont();

            // Titre
            Paragraph title = new Paragraph("Biological Data")
                    .setFont(titleFont)
                    .setFontSize(36)
                    .setFontColor(titleColor)
                    .setBold()
                    .setItalic()
                    .setMarginBottom(20) // Ajoute une marge en bas du titre
                    .setTextAlignment(TextAlignment.CENTER); // Centrer le titre horizontalement

            // Ajout de l'image en haut du document
            Image logo = new Image(ImageDataFactory.create("src/main/resources/img/logo.png"));

            // Création d'une ligne contenant le logo et le titre
            Div headerDiv = new Div()
                    .add(logo)
                    .add(title)
                    .setHorizontalAlignment(HorizontalAlignment.LEFT); // Alignement à gauche

            document.add(headerDiv);

            // Tableau pour afficher les données biologiques
            Table table = new Table(new float[]{2, 3, 3, 3, 2, 3, 4});
            // Appliquer la couleur de fond pour les cellules du tableau
            table.setBackgroundColor(cellColor);

            // Modification des couleurs et de la police pour les titres de colonnes
            for (int i = 0; i < 7; i++) {
                table.addCell(new Paragraph(getHeaderTitle(i))
                        .setFont(contentFont)
                        .setBold()
                        .setFontColor(textColor)
                        .setBackgroundColor(headerColor)
                        .setPadding(8) // Ajuste le padding pour les titres de colonnes
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(new SolidBorder(1))); // Ajoute une bordure blanche autour des titres de colonnes
            }

            // Ajoutez les données biologiques au tableau avec des bordures et des marges personnalisées
            for (BiologicalData data : dataList) {
                addCellWithBorder(table, data.getMeasurementType(), contentFont, textColor);
                addCellWithBorder(table, data.getValue(), contentFont, textColor);
                addCellWithBorder(table, data.getPatientName(), contentFont, textColor);
                addCellWithBorder(table, data.getPatientLastName(), contentFont, textColor);
                addCellWithBorder(table, String.valueOf(data.getPatientAge()), contentFont, textColor);
                addCellWithBorder(table, data.getDisease(), contentFont, textColor);
                addCellWithBorder(table, data.getOtherInformation(), contentFont, textColor);
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

    // Méthode utilitaire pour ajouter une cellule avec des bordures au tableau
    private static void addCellWithBorder(Table table, String content, PdfFont font, Color textColor) {
        table.addCell(new Paragraph(content)
                .setFont(font)
                .setFontColor(textColor)
                .setPadding(8) // Ajuste le padding pour le contenu des cellules
                .setBorder(new SolidBorder( 1)) // Ajoute une bordure blanche autour de la cellule
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)); // Centrer le texte horizontalement
    }
}
