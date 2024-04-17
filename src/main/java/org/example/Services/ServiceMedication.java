package org.example.Services;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.Interfaces.Iservice;
import org.example.Utils.MyDataBase;
import org.example.models.Medication;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceMedication implements Iservice<Medication> {
    private Connection cnx;

    public ServiceMedication() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Medication medication) {
        String qry = "INSERT INTO medication (name_medication, description, medical_note, dosage) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, medication.getNameMedication());
            pstmt.setString(2, medication.getDescription());
            pstmt.setString(3, medication.getMedicalNote());
            pstmt.setString(4, medication.getDosage());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'insertion du médicament a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("ID généré pour le médicament : " + generatedId);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID généré pour le médicament.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Medication> getAll() {
        ArrayList<Medication> medications = new ArrayList<>();
        String qry = "SELECT * FROM medication";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Medication m = new Medication();
                m.setId(rs.getInt("id"));
                m.setNameMedication(rs.getString("name_medication"));
                m.setDescription(rs.getString("description"));
                m.setMedicalNote(rs.getString("medical_note"));
                m.setDosage(rs.getString("dosage"));

                medications.add(m);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return medications;
    }


    @Override
    public void update(Medication medication) {
        String qry = "UPDATE medication SET name_medication=?, description=?, medical_note=?, dosage=? WHERE id=?";
        try {
            PreparedStatement pstmt = cnx.prepareStatement(qry);
            pstmt.setString(1, medication.getNameMedication());
            pstmt.setString(2, medication.getDescription());
            pstmt.setString(3, medication.getMedicalNote());
            pstmt.setString(4, medication.getDosage());
            pstmt.setInt(5, medication.getId());
            pstmt.executeUpdate();
            System.out.println("Médication mise à jour avec succès.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public boolean delete(Medication medication) {
        String qry = "DELETE FROM medication WHERE id=?";
        try {
            PreparedStatement pstmt = cnx.prepareStatement(qry);
            pstmt.setInt(1, medication.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Médication supprimée avec succès.");
                return true;
            } else {
                System.out.println("Aucune médication trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void generateMedicationDetails(Medication m) {
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




