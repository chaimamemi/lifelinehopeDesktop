package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import models.Appointment;
import models.User; // Assurez-vous que cette classe existe dans votre package models
import services.AppointmentService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddAppointmentController {

    @FXML
    private TextField tfPatientId;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField tfTime;

    @FXML
    private TextArea taDescription;

    @FXML
    private TextField tfStatus;

    @FXML
    private TextField tfDoctorId;

    private final AppointmentService appointmentService = new AppointmentService();

    @FXML
    void addAppointment(ActionEvent event) {
        try {
            Appointment newAppointment = new Appointment();
            newAppointment.setPatientId(Integer.parseInt(tfPatientId.getText()));
            LocalDateTime dateTime = LocalDateTime.of(dpDate.getValue(), LocalTime.parse(tfTime.getText()));
            newAppointment.setDateTime(dateTime);
            newAppointment.setDescription(taDescription.getText());
            newAppointment.setStatus(tfStatus.getText());
            newAppointment.setDoctorId(Integer.parseInt(tfDoctorId.getText()));

            // Simuler la création d'un utilisateur (cela devrait être l'utilisateur actuellement connecté)
            User currentUser = new User(); // Assurez-vous que la classe User a un constructeur par défaut
            currentUser.setRole("ROLE_OWNER"); // Assurez-vous que la classe User a une méthode setRole

            // Vérifiez que votre service AppointmentService a une méthode add qui prend un objet Appointment et User
            appointmentService.add(newAppointment, currentUser);

            // Affichez un message de succès ou nettoyez le formulaire comme nécessaire
            // ...

        } catch (NumberFormatException | DateTimeParseException e) {
            // Gérer les exceptions, comme NumberFormatException si les champs ID ne sont pas des entiers
            // ou DateTimeParseException si l'heure n'est pas au format correct
            System.out.println("Erreur de format : " + e.getMessage());
            // Afficher un message d'erreur à l'utilisateur
            // ...
        } catch (Exception e) {
            // Gérer les autres exceptions inattendues
            System.out.println("Erreur inattendue : " + e.getMessage());
            // Afficher un message d'erreur à l'utilisateur
            // ...
        }
    }

    // Méthodes pour nettoyer le formulaire et afficher des messages
    private void clearForm() {
        // Nettoyer les champs de texte
        tfPatientId.clear();
        tfTime.clear();
        taDescription.clear();
        tfStatus.clear();
        tfDoctorId.clear();
        dpDate.setValue(null);
    }

    private void displaySuccessMessage() {
        // Afficher un message de succès
        System.out.println("Le rendez-vous a été ajouté avec succès.");
    }

    private void displayErrorMessage() {
        // Afficher un message d'erreur
        System.out.println("Erreur lors de l'ajout du rendez-vous.");
    }
}
