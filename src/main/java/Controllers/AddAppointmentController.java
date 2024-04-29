package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
            showAlert(AlertType.ERROR, "Error", "Required Fields Not Filled", "Please fill in all required fields.");
            return;
        }

        if (!patientEmail.contains("@")) {
            showAlert(AlertType.ERROR, "Error", "Invalid Email", "Email must contain the character '@'.");
            return;
        }

        String description = descriptionTextArea.getText().trim();
        if (description.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Missing Description", "Please provide a description.");
            return;
        }

        if (!isConfirmedCheckBox.isSelected()) {
            showAlert(AlertType.INFORMATION, "Information", "Confirmation Required", "You must confirm if you want to add an appointment.");
            return;
        }

        try {
            int patientId = appointmentService.getPatientIdByUserName(patientName);
            String userRole = appointmentService.getRoleById(patientId);

            if (!userRole.equals("ROLE_OWNER")) {
                showAlert(AlertType.ERROR, "Error", "Unauthorized Access", "Only owners can create appointments.");
                return;
            }

            int doctorId = appointmentService.getDoctorIdByEmail(doctorEmail);

            if (patientId == -1 || doctorId == -1) {
                showAlert(AlertType.ERROR, "Error", "Invalid Data", "Invalid patient name or doctor email.");
                return;
            }

            System.out.println("Patient ID: " + patientId + ", Doctor ID: " + doctorId);

            Appointment appointment = new Appointment();
            appointment.setPatientId(patientId);
            appointment.setDateTime(selectedDate.atStartOfDay());
            appointment.setDescription(description);
            appointment.setDoctorId(doctorId);
            appointment.setStatus("Pending");

            appointment.setIsConfirmed(true); // Force confirmation for addition

            appointment.setIsUrgent(isUrgentCheckBox.isSelected());

            appointmentService.add(appointment, currentUser);

            showAlert(AlertType.INFORMATION, "Success", "Appointment Added", "The appointment has been added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Addition Failed", "Failed to add the appointment.");
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

        // Make sure the resource path is correct and the FXML file is located in the resources folder.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheAppointement.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) backaff.getScene().getWindow(); // Get the current button's window
            stage.setScene(scene); // Set the newly loaded scene for the window
            stage.show(); // Show the window with the new scene
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging.
            // Here you can handle the exception, like showing an alert to the user.
        }
    }

}
