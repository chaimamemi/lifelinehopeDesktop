package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.Appointment;
import models.User;
import services.AppointmentService;
import javafx.scene.control.Button;


import java.io.IOException;
import java.time.LocalDate;

import java.io.InputStream;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.List;

public class AddAppointmentController {

    private AppointmentService appointmentService = new AppointmentService();

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button addbouton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private ComboBox<String> doctorComboBox;

    @FXML
    private ImageView doctorImageView;

    @FXML
    private CheckBox isConfirmedCheckBox;

    @FXML
    private CheckBox isUrgentCheckBox;
    @FXML
    private int currentAppointmentId;



    @FXML
    private Button backaff;




    @FXML
    public void initialize() {
        loadImage();
        populateDoctorsComboBox();
        backaff.setOnAction(this::backAfficheAppointment);
    }

    private void loadImage() {
        try {
            String imagePath = "/doctor_appointment_app_img.png";
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                doctorImageView.setImage(image);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error while loading the image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateDoctorsComboBox() {
        List<String> doctorEmails = appointmentService.getAllDoctorEmails();
        ObservableList<String> observableDoctorEmails = FXCollections.observableArrayList(doctorEmails);
        doctorComboBox.setItems(observableDoctorEmails);
    }

    @FXML
    void addAppointment() {
        String patientName = nameTextField.getText().trim();
        String patientEmail = emailTextField.getText().trim();
        String doctorEmail = doctorComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();
        User currentUser = getCurrentUser();

        if (patientName.isEmpty() || patientEmail.isEmpty() || doctorEmail == null || selectedDate == null) {
            showAlert(AlertType.ERROR, "Erreur", "Champs obligatoires non saisis", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (!patientEmail.contains("@")) {
            showAlert(AlertType.ERROR, "Erreur", "Email invalide", "L'email doit contenir le caractère '@'.");
            return;
        }

        String description = descriptionTextArea.getText().trim();
        if (description.isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur", "Description manquante", "Veuillez fournir une description.");
            return;
        }

        if (!isConfirmedCheckBox.isSelected()) {
            showAlert(AlertType.INFORMATION, "Information", "Confirmation requise", "Si vous souhaitez ajouter un rendez-vous, vous devez confirmer.");
            return;
        }

        try {
            int patientId = appointmentService.getPatientIdByUserName(patientName);
            String userRole = appointmentService.getRoleById(patientId);

            if (!userRole.equals("ROLE_OWNER")) {
                showAlert(AlertType.ERROR, "Erreur", "Accès non autorisé", "Seuls les propriétaires peuvent créer des rendez-vous.");
                return;
            }

            int doctorId = appointmentService.getDoctorIdByEmail(doctorEmail);

            if (patientId == -1 || doctorId == -1) {
                showAlert(AlertType.ERROR, "Erreur", "Données invalides", "Nom du patient ou email du médecin invalide.");
                return;
            }

            System.out.println("Patient ID: " + patientId + ", Doctor ID: " + doctorId);

            Appointment appointment = new Appointment();
            appointment.setPatientId(patientId);
            appointment.setDateTime(selectedDate.atStartOfDay());
            appointment.setDescription(description);
            appointment.setDoctorId(doctorId);
            appointment.setStatus("Pending");

            appointment.setIsConfirmed(true); // Forcer la confirmation pour l'ajout

            appointment.setIsUrgent(isUrgentCheckBox.isSelected());

            appointmentService.add(appointment, currentUser);

            showAlert(AlertType.INFORMATION, "Succès", "Rendez-vous ajouté", "Le rendez-vous a été ajouté avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Échec de l'ajout", "Impossible d'ajouter le rendez-vous.");
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private User getCurrentUser() {
        User currentUser = new User();
        currentUser.setRole("ROLE_OWNER");
        return currentUser;
    }



    @FXML
    void backAfficheAppointment(ActionEvent event) {

        // Assurez-vous que le chemin de ressource est correct et que le fichier FXML est situé dans le dossier de ressources.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheAppointement.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) backaff.getScene().getWindow(); // Obtenez la fenêtre actuelle du bouton
            stage.setScene(scene); // Définissez la nouvelle scène chargée pour la fenêtre
            stage.show(); // Affichez la fenêtre avec la nouvelle scène
        } catch (IOException e) {
            e.printStackTrace(); // Imprimez la trace de la pile d'exceptions pour le débogage.
            // Ici, vous pouvez gérer l'exception, comme afficher une alerte à l'utilisateur.
        }
    }

    }

