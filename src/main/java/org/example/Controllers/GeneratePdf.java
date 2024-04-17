package org.example.Controllers;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.models.Medication;

import java.io.IOException;

public class GeneratePdf {
    public static void generateMedicationDetails(Medication m) {
        try {
            // Crée un nouveau document PDF
            PDDocument document = new PDDocument();

            // Ajoute une page au document
            PDPage page = new PDPage();
            document.addPage(page);

            // Crée un objet PDPageContentStream pour écrire du contenu dans la page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Écrit du texte dans la page avec les détails de la médication
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Détails de la médication :");
            contentStream.newLine();
            contentStream.showText("Nom du médicament : " + m.getNameMedication());
            contentStream.newLine();
            contentStream.showText("Description : " + m.getDescription());
            contentStream.newLine();
            contentStream.showText("Note médicale : " + m.getMedicalNote());
            contentStream.newLine();
            contentStream.showText("Dosage : " + m.getDosage());
            contentStream.endText();

            // Ferme le flux de contenu
            contentStream.close();

            // Sauvegarde le document PDF
            document.save("medication_details.pdf");

            // Ferme le document
            document.close();

            System.out.println("Le PDF a été généré avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }
}
