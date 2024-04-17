package org.example;
import org.example.Controllers.GeneratePdf;
import org.example.Services.ServiceBiologicalData;
import org.example.Services.ServiceMedication;
import org.example.models.BiologicalData;
import org.example.models.Medication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {


        Medication m = new Medication();
        m.setId(2);
        m.setNameMedication("Panadol");
        m.setDescription("for cardiology ");
        m.setMedicalNote("read carefully the notice");
        m.setDosage("20mg");


        ServiceMedication sp = new ServiceMedication();
        sp.add(m);
        ArrayList<Medication> medications = sp.getAll();
        for (Medication medication : medications) {
            System.out.println(medication);
        }
        ;
// Mettez à jour une médication  dans la base de données
        Medication medicationToUpdate = new Medication();
        medicationToUpdate.setId(94);
        medicationToUpdate.setNameMedication("Updated Name");
        medicationToUpdate.setDescription("Updated Description");
        medicationToUpdate.setMedicalNote("Updated Medical Note");
        medicationToUpdate.setDosage("Updated Dosage");

        sp.update(medicationToUpdate);


        Medication medicationToDelete = new Medication();
        medicationToDelete.setId(94);

        boolean isDeleted = sp.delete(medicationToDelete);
        if (isDeleted) {
            System.out.println("Médication supprimée avec succès.");
        } else {
            System.out.println("Échec de la suppression de la médication.");
        }

        ServiceBiologicalData service = new ServiceBiologicalData();


        BiologicalData data = new BiologicalData(
                4,          // id

                "Type",      // measurementType
                "Value",     // value
                "",      // patientName
                "",       // patientLastName
                23,          // patientAge
                "Diabitic",// disease
                "iii"


        );

        // Créez une instance de ServiceBiologicalData pour interagir avec la base de données
        ServiceBiologicalData spp = new ServiceBiologicalData();
        spp.add(data);


        // Récupérez toutes les données biologiques de la base de données
        ArrayList<BiologicalData> biologicalDataList = spp.getAll();

        // Affichez les données récupérées
        for (BiologicalData biologicalData : biologicalDataList) {
            System.out.println(biologicalData);
        }
// Mettez à jour les données biologiques spécifiques dans la base de données
        BiologicalData biologicalDataToUpdate = new BiologicalData();
        biologicalDataToUpdate.setId(4); // ID des données biologiques à mettre à jour
        biologicalDataToUpdate.setMeasurementType("Updated Type");
        biologicalDataToUpdate.setValue("20");
        biologicalDataToUpdate.setPatientName("Updated Patient Name");
        biologicalDataToUpdate.setPatientLastName("Updated Patient Last Name");
        biologicalDataToUpdate.setPatientAge(30); // Âge mis à jour
        biologicalDataToUpdate.setDisease("Updated Disease");
        biologicalDataToUpdate.setOtherInformation("Updated Other Information");

// Appel à la méthode update de ServiceBiologicalData pour mettre à jour les données biologiques
        spp.update(biologicalDataToUpdate);
        // Supprimer des données biologiques spécifiques de la base de données
        BiologicalData dataToDelete = new BiologicalData();
        dataToDelete.setId(3); // ID des données biologiques à supprimer

// Appel à la méthode delete de ServiceBiologicalData pour supprimer les données biologiques
        boolean isd = spp.delete(dataToDelete);
        if (isDeleted) {
            System.out.println("Données biologiques supprimées avec succès.");
        } else {
            System.out.println("Échec de la suppression des données biologiques.");
        }
        // Appel à la méthode pour générer le PDF
        ArrayList<Medication> medications1 = sp.getAll();
        GeneratePdf.generateMedicationDetails(m);


        // Appel à la méthode filterMedications pour filtrer les médications
        List<Medication> filteredMedications = filterMedications(medications1, "Panadol", "20mg", "cardiology");
        // Affichez les médications filtrées
        for (Medication filteredMedication : filteredMedications) {
            System.out.println(filteredMedication);
        }
    }

    // Méthode pour filtrer les médications
    public static List<Medication> filterMedications(List<Medication> medications, String name, String dosage, String description) {
        // Construisez vos critères de filtrage
        return medications.stream()
                .filter(medication -> medication.getNameMedication().equals(name) &&
                        medication.getDosage().equals(dosage) &&
                        medication.getDescription().contains(description))
                .collect(Collectors.toList());
    }



}





