package org.example.models;

public class Medication {

    private int id;
    private String nameMedication;
    private String description;
    private String medicalNote;
    private String dosage;


    public Medication() {
        this.description = this.description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameMedication() {
        return this.nameMedication;
    }

    public void setNameMedication(String nameMedication) {
        if (nameMedication != null && !nameMedication.trim().isEmpty()) {
            this.nameMedication = nameMedication;
        } else {
            throw new IllegalArgumentException("Le nom de la médication ne peut pas être vide.");
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }
    }

    public String getMedicalNote() {
        return this.medicalNote;
    }

    public void setMedicalNote(String medicalNote) {
        if (medicalNote != null && !medicalNote.trim().isEmpty()) {
            this.medicalNote = medicalNote;
        } else {
            throw new IllegalArgumentException("La note médicale ne peut pas être vide.");
        }
    }

    public String getDosage() {
        return this.dosage;
    }

    public void setDosage(String dosage) {
        if (dosage != null && !dosage.trim().isEmpty()) {
            this.dosage = dosage;
        } else {
            throw new IllegalArgumentException("Le dosage ne peut pas être vide.");
        }
    }
}

