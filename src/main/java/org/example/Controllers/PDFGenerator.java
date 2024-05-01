package org.example.Controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.models.BiologicalData;

import java.io.IOException;

public class PDFGenerator {
    public static void generateBiologicalData(BiologicalData data) {
        try {
            // Crée un nouveau document PDF
            PDDocument document = new PDDocument();

            // Ajoute une page au document
            PDPage page = new PDPage();
            document.addPage(page);

            // Crée un objet PDPageContentStream pour écrire du contenu dans la page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Écrit du texte dans la page avec les détails des données biologiques
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Détails des données biologiques :");
            contentStream.newLine();
            contentStream.showText("Type de mesure : " + data.getMeasurementType());
            contentStream.newLine();
            contentStream.showText("Valeur : " + data.getValue());
            contentStream.newLine();
            contentStream.showText("Nom du patient : " + data.getPatientName());
            contentStream.newLine();
            contentStream.showText("Nom de famille du patient : " + data.getPatientLastName());
            contentStream.newLine();
            contentStream.showText("Âge du patient : " + data.getPatientAge());
            contentStream.newLine();
            contentStream.showText("Maladie : " + data.getDisease());
            contentStream.newLine();
            contentStream.showText("Autres informations : " + data.getOtherInformation());
            contentStream.endText();

            // Ferme le flux de contenu
            contentStream.close();

            // Sauvegarde le document PDF
            document.save("biological_data_details.pdf");

            // Ferme le document
            document.close();

            System.out.println("Le PDF des données biologiques a été généré avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du PDF des données biologiques : " + e.getMessage());
        }
    }
}
