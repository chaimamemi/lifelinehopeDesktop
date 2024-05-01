package org.example.Controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;

import org.example.models.BiologicalData;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFGenerator {

    public void generatePDF(List<BiologicalData> dataList) {
        String projectDirectory = System.getProperty("user.dir");
        String filePath = projectDirectory + File.separator + "pdf" + File.separator + "biological_data.pdf";

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Ajouter le contenu du PDF
            addContent(document, dataList);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addContent(Document document, List<BiologicalData> dataList) {
        // Ajouter le titre de la section
        Paragraph title = new Paragraph("Biological Data Details");
        title.setFontSize(18).setBold().setUnderline();
        document.add(title);

        document.add(new Paragraph("\n")); // Ajouter un espace vertical


    }
}
