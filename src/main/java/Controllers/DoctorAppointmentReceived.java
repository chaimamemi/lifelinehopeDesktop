package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import models.Appointment;
import models.User;
import services.AppointmentService;

import java.util.List;

public class DoctorAppointmentReceived {

    private final AppointmentService appointmentService = new AppointmentService();

    @FXML
    private Button accept;

    @FXML
    private ListView<Appointment> appointmentReceived;

    @FXML
    public void initialize() {
        // Load pending appointments and display them in the list
        List<Appointment> pendingAppointments = appointmentService.getAllPendingAppointments();
        appointmentReceived.getItems().addAll(pendingAppointments);

        // Set custom cell for display in the ListView
        appointmentReceived.setCellFactory(listView -> new ListCell<Appointment>() {
            @Override
            protected void updateItem(Appointment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    TextFlow textFlow = new TextFlow();

                    Text patientNameLabel = new Text("Nom Patient: ");
                    patientNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                    Text patientNameValue = new Text(appointmentService.getPatientNameById(item.getPatientId()) + "\n");
                    patientNameValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    patientNameValue.setFill(Color.BLUE);

                    Text descriptionLabel = new Text("Description: ");
                    descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                    Text descriptionValue = new Text(item.getDescription() + "\n");
                    descriptionValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    descriptionValue.setFill(Color.BLUE);

                    Text dateTimeLabel = new Text("Date & Heure: ");
                    dateTimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                    Text dateTimeValue = new Text(item.getDateTime().toString() + "\n");
                    dateTimeValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    dateTimeValue.setFill(Color.BLUE);

                    Text urgencyLabel = new Text("Urgence: ");
                    urgencyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                    Text urgencyValue = new Text((item.getIsUrgent() ? "Urgent" : "Not Urgent") + "\n");
                    urgencyValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    urgencyValue.setFill(Color.BLUE);

                    textFlow.getChildren().addAll(patientNameLabel, patientNameValue, descriptionLabel, descriptionValue, dateTimeLabel, dateTimeValue, urgencyLabel, urgencyValue);
                    setGraphic(textFlow);
                }
            }
        });
    }

    @FXML
    void respondToAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentReceived.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser(); // Method to get the current user

        if (selectedAppointment != null) {
            Button sourceButton = (Button) event.getSource();
            String statusToUpdate = "";
            if (sourceButton == accept) {
                statusToUpdate = "Accepted";
            } else {
                statusToUpdate = "Refused";
            }

            if (!statusToUpdate.isEmpty()) {
                boolean updated = appointmentService.respondToAppointment(selectedAppointment.getId(), statusToUpdate, currentUser);
                if (updated) {
                    selectedAppointment.setStatus(statusToUpdate);
                    appointmentReceived.refresh();
                    showAlert("Success", "Appointment Updated", "Appointment status updated successfully.");
                } else {
                    showAlert("Error", "Appointment Update Failed", "Failed to update appointment status.");
                }
            }
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private User getCurrentUser() {
        User currentUser = new User();
        currentUser.setRole("ROLE_DOCTOR");
        return currentUser;
    }
}
