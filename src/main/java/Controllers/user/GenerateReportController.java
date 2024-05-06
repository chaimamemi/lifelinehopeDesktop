package Controllers.user;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.HealthMetrics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import services.BraceletService;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class GenerateReportController {


    @FXML
    private Button boutonAffiche;

    @FXML
    private Button downloadButton;
    @FXML
    private TextArea reportTextArea;
    private BraceletService braceletService = new BraceletService();
    private HealthMetrics metrics;

    @FXML
    public void initialize() {
        loadReport();
        reportTextArea.setEditable(false);
        boutonAffiche.setOnAction(this::handleDataBraceletHistory);
    }

    private void loadReport() {
        metrics = braceletService.generateAdvancedHealthAnalysis();
        String formattedReport = formatReportForDisplay(metrics);
        reportTextArea.setText(formattedReport);
    }

    private String formatReportForDisplay(HealthMetrics metrics) {
        StringBuilder displayText = new StringBuilder();
        displayText.append("Advanced Health Analysis Report\n\n");
        displayText.append("Average Temperature: ").append(String.format("%.1f°C (%.2f%%)", metrics.getAverageTemperature(), metrics.getTemperaturePercentage()));
        displayText.append("\nAverage Heart Rate: ").append(String.format("%.0f bpm (%.2f%%)", metrics.getAverageHeartRate(), metrics.getHeartRatePercentage()));
        displayText.append("\nAverage Blood Pressure: ").append(String.format("%.0f mmHg (%.2f%%)", metrics.getAverageBloodPressure(), metrics.getBloodPressurePercentage()));
        displayText.append("\n\nHealth Advice:\n").append(metrics.getHealthAdvice());
        return displayText.toString();
    }

    private JFreeChart createPieChart(HealthMetrics metrics) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Temperature", metrics.getAverageTemperature());
        dataset.setValue("Heart Rate", metrics.getAverageHeartRate());
        dataset.setValue("Blood Pressure", metrics.getAverageBloodPressure());

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setSectionPaint("Temperature", new java.awt.Color(204, 229, 255));
        plot.setSectionPaint("Heart Rate", new java.awt.Color(255, 204, 204));
        plot.setSectionPaint("Blood Pressure", new java.awt.Color(204, 255, 204));
        plot.setExplodePercent("Temperature", 0.10);
        plot.setExplodePercent("Heart Rate", 0.10);
        plot.setExplodePercent("Blood Pressure", 0.10);
        plot.setSimpleLabels(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}")); // Afficher le pourcentage
        return chart;
    }


    private void saveChartAsPNG(JFreeChart chart, Document document) throws IOException {
        ByteArrayOutputStream chartStream = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(chartStream, chart, 500, 300);
        ImageData chartImage = ImageDataFactory.create(chartStream.toByteArray());
        document.add(new Image(chartImage));
    }

    @FXML
    void downloadReport(ActionEvent event) {
        String dest = System.getProperty("user.home") + "\\Downloads\\report.pdf";
        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Advanced Health Analysis Report")
                    .setFontColor(new DeviceRgb(0, 153, 204))
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold());
            document.add(new Paragraph("\n"));

            // Create the table with the appropriate column widths
            Table table = new Table(new float[]{1, 2});
            table.setBackgroundColor(DeviceRgb.WHITE); // Set the background color for the whole table

            // Adding headers with a background color
            Cell cell = new Cell().add(new Paragraph("Metric"))
                    .setBackgroundColor(new DeviceRgb(230, 230, 250))
                    .setBorderLeft(new SolidBorder(new DeviceRgb(0, 153, 204), 1))
                    .setBorderRight(new SolidBorder(new DeviceRgb(0, 153, 204), 1));
            table.addCell(cell);

            cell = new Cell().add(new Paragraph("Value"))
                    .setBackgroundColor(new DeviceRgb(230, 230, 250))
                    .setBorderRight(new SolidBorder(new DeviceRgb(0, 153, 204), 1));
            table.addCell(cell);

            // Data rows
            addDataCell(table, "Average Temperature", String.format("%.1f°C (%.2f%%)", metrics.getAverageTemperature(), metrics.getTemperaturePercentage()));
            addDataCell(table, "Average Heart Rate", String.format("%.0f bpm (%.2f%%)", metrics.getAverageHeartRate(), metrics.getHeartRatePercentage()));
            addDataCell(table, "Average Blood Pressure", String.format("%.0f mmHg (%.2f%%)", metrics.getAverageBloodPressure(), metrics.getBloodPressurePercentage()));

            document.add(table);

            document.add(new Paragraph("Health Advice:")
                    .setBold()
                    .setFontColor(new DeviceRgb(0, 102, 204))
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph(metrics.getHealthAdvice())
                    .setFontColor(new DeviceRgb(128, 0, 0))
                    .setTextAlignment(TextAlignment.LEFT));

            JFreeChart chart = createPieChart(metrics);
            saveChartAsPNG(chart, document);

            System.out.println("Report saved as PDF: " + dest);
            Desktop.getDesktop().open(new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDataCell(Table table, String label, String value) {
        Cell cell = new Cell().add(new Paragraph(label))
                .setBackgroundColor(new DeviceRgb(255, 255, 255)) // White background for data cells
                .setBorderLeft(new SolidBorder(new DeviceRgb(0, 153, 204), 1))
                .setBorderBottom(new SolidBorder(new DeviceRgb(0, 153, 204), 1))
                .setBorderRight(new SolidBorder(new DeviceRgb(0, 153, 204), 1));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph(value))
                .setBackgroundColor(new DeviceRgb(255, 255, 255)) // White background for data cells
                .setBorderRight(new SolidBorder(new DeviceRgb(0, 153, 204), 1))
                .setBorderBottom(new SolidBorder(new DeviceRgb(0, 153, 204), 1));
        table.addCell(cell);
    }




    @FXML
    void handleDataBraceletHistory(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/afficheAllBraceletData.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) boutonAffiche.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
