package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import services.AppointmentService;
import models.Appointment;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateAppointmentController {

    @FXML
    private Button updateButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionText;

    @FXML
    private TextField nameText;


    @FXML
    private Button CancelButton1;

    private final AppointmentService appointmentService = new AppointmentService();
    private int currentAppointmentId; // ID of the appointment being updated



    @FXML
    public void initialize() {

        CancelButton1.setOnAction(this:: CancelAppointement);
    }

    public void initData(int appointmentId) {
        this.currentAppointmentId = appointmentId;
        // Get the current appointment details and populate the fields
        Appointment currentAppointment = appointmentService.getAppointmentById(appointmentId);
        if (currentAppointment != null) {
            nameText.setText(appointmentService.getPatientNameById(currentAppointment.getPatientId()));
            descriptionText.setText(currentAppointment.getDescription());
            datePicker.setValue(currentAppointment.getDateTime().toLocalDate());
        }
    }

    @FXML
    void updateAppointment(ActionEvent event) throws SQLException {
        String patientName = nameText.getText();
        String description = descriptionText.getText();
        LocalDate date = datePicker.getValue();

        // Perform the update
        boolean updateSuccessful = appointmentService.updateAppointment(currentAppointmentId, patientName, description, date);

        if (updateSuccessful) {
            // Navigate back to the appointment list view
            navigateBack();
        } else {
            // Handle the error, maybe show a message to the user
            System.out.println("Update failed. Please try again.");
        }
    }

    private void navigateBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheAppointement.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void CancelAppointement(ActionEvent event) {
        // Make sure the resource path is correct and the FXML file is located in the resources folder.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheAppointement.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) CancelButton1.getScene().getWindow(); // Get the current button's window
            stage.setScene(scene); // Set the newly loaded scene for the window
            stage.show(); // Show the window with the new scene
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging.
            // Here you can handle the exception, like showing an alert to the user.
        }
    }

    }


