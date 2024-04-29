package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Appointment;
import models.User;
import services.AppointmentService;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class AfficheAppointementController {

    @FXML
    private ListView<String> appointmentRequest; // ListView to display appointments

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    private final AppointmentService appointmentService = new AppointmentService(); // Service to manage appointments

    @FXML
    private void initialize() {
        displayAppointments(); // Call method to display appointments when the controller starts
        add.setOnAction(this::addapp);
        delete.setOnAction(this::deleteapp);
        update.setOnAction(this::updateapp);
    }

    private void displayAppointments() {
        List<Appointment> appointments = appointmentService.getAllPendingAppointments();
        appointmentRequest.getItems().clear();

        appointmentRequest.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    TextFlow textFlow = new TextFlow();
                    String[] parts = item.split(", ");
                    for (String part : parts) {
                        // Split label and content
                        String[] labelAndContent = part.split(": ", 2);
                        Text labelText = new Text(labelAndContent[0] + ": ");
                        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                        Text contentText = new Text(labelAndContent.length > 1 ? labelAndContent[1] : "");
                        contentText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                        // Specific condition for status color
                        if (labelAndContent[0].equals("Status")) {
                            String statusValue = labelAndContent[1].trim();
                            if ("Accepted".equalsIgnoreCase(statusValue)) {
                                contentText.setFill(Color.GREEN);
                            } else {
                                contentText.setFill(Color.RED);
                            }
                        } else {
                            // For other values, apply blue color
                            contentText.setFill(Color.BLUE);
                        }

                        // Add Text to TextFlow
                        textFlow.getChildren().addAll(labelText, contentText);

                        // Separator after each part
                        Text separator = new Text(", ");
                        separator.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                        textFlow.getChildren().add(separator);
                    }

                    // Remove the last separator
                    if (!textFlow.getChildren().isEmpty()) {
                        textFlow.getChildren().remove(textFlow.getChildren().size() - 1);
                    }

                    setGraphic(textFlow);
                }
            }
        });

        for (Appointment appointment : appointments) {
            String patientName = appointmentService.getPatientNameById(appointment.getPatientId());
            String doctorEmail = appointmentService.getDoctorEmailById(appointment.getDoctorId());
            String status = appointment.getStatus();
            String description = appointment.getDescription(); // Add description

            // Modify the order of elements in the appointmentInfo string
            String appointmentInfo = String.format("Name Patient: %s, Date & Time: %s, Doctor Email: %s, Description: %s, Status: %s",
                    patientName, appointment.getDateTime(), doctorEmail, description, status);

            appointmentRequest.getItems().add(appointmentInfo);
        }
    }

    @FXML
    void addapp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddApoitment.fxml"));
        try {
            Parent root = loader.load();
            AddAppointmentController controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) add.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deleteapp(ActionEvent event) {
        int appointmentId = extractAppointmentIdFromSelection();
        if (appointmentId != -1) {
            Appointment appointmentToDelete = new Appointment();
            appointmentToDelete.setId(appointmentId);

            User currentUser = getCurrentUser();
            if (currentUser == null || !currentUser.getRole().equals("ROLE_OWNER")) {
                showAlert("You don't have the rights to delete this appointment.");
                return;
            }

            boolean isDeleted = appointmentService.delete(appointmentToDelete, currentUser);

            if (isDeleted) {
                displayAppointments();
            } else {
                showAlert("Failed to delete the appointment.");
            }
        } else {
            showAlert("Error extracting appointment ID.");
        }
    }

    private int extractAppointmentIdFromSelection() {
        try {
            int selectedIndex = appointmentRequest.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("No appointment selected for deletion.");
                return -1;
            }

            List<Appointment> appointments = appointmentService.getAllPendingAppointments();
            if (selectedIndex >= 0 && selectedIndex < appointments.size()) {
                return appointments.get(selectedIndex).getId();
            } else {
                showAlert("Invalid selected appointment index.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private User getCurrentUser() {
        User currentUser = new User();
        currentUser.setRole("ROLE_OWNER");
        return currentUser;
    }

    @FXML
    void updateapp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateAppointment.fxml"));
        try {
            Parent root = loader.load();
            UpdateAppointmentController controller = loader.getController();

            int appointmentId = extractAppointmentIdFromSelection();
            if (appointmentId != -1) {
                controller.initData(appointmentId);

                Scene scene = new Scene(root);
                Stage stage = (Stage) update.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert("Error extracting appointment ID.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
