package Controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.Appointment;
import models.User;
import services.AppointmentService;

import java.io.IOException;
import java.util.List;

public class AfficheAppointementController {

    @FXML
    private ListView<String> appointmentRequest; // ListView to display appointments

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    @FXML
    private Button calendar;

    @FXML
    private TextField searchfx;

    @FXML
    private Button chat;

    private final AppointmentService appointmentService = new AppointmentService();
    private ObservableList<String> allAppointments; // Stores all appointment strings for searching

    @FXML
    private void initialize() {
        displayAppointments();
        setupSearchFilter();
        add.setOnAction(this::addapp);
        delete.setOnAction(this::deleteapp);
        update.setOnAction(this::updateapp);
        chat.setOnAction(this::chat);
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
                        String[] labelAndContent = part.split(": ", 2);
                        Text labelText = new Text(labelAndContent[0] + ": ");
                        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                        Text contentText = new Text(labelAndContent.length > 1 ? labelAndContent[1] : "");
                        contentText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                        if ("Status".equals(labelAndContent[0])) {
                            contentText.setFill("Accepted".equalsIgnoreCase(labelAndContent[1].trim()) ? Color.GREEN : Color.RED);
                        } else {
                            contentText.setFill(Color.BLUE);
                        }

                        textFlow.getChildren().addAll(labelText, contentText, new Text(", "));
                    }
                    if (!textFlow.getChildren().isEmpty()) {
                        textFlow.getChildren().remove(textFlow.getChildren().size() - 1);
                    }
                    setGraphic(textFlow);
                }
            }
        });

        for (Appointment appointment : appointments) {
            String appointmentInfo = formatAppointmentString(appointment);
            appointmentRequest.getItems().add(appointmentInfo);
        }

        allAppointments = FXCollections.observableArrayList(appointmentRequest.getItems());
    }

    private String formatAppointmentString(Appointment appointment) {
        return String.format("Name Patient: %s, Date & Time: %s, Doctor Email: %s, Description: %s, Status: %s",
                appointmentService.getPatientNameById(appointment.getPatientId()),
                appointment.getDateTime(),
                appointmentService.getDoctorEmailById(appointment.getDoctorId()),
                appointment.getDescription(),
                appointment.getStatus());
    }

    private void setupSearchFilter() {
        searchfx.textProperty().addListener((observable, oldValue, newValue) -> {
            if (allAppointments != null) {
                appointmentRequest.setItems(allAppointments.filtered(item -> item.toLowerCase().contains(newValue.toLowerCase())));
            }
        });
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
            showAlert("please choose one.");
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
                showAlert("please choose one.");
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

    @FXML
    void ViewCalander(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendarOwner.fxml"));
        try {
            Parent root = loader.load();
            CalendarController controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) add.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void chat(ActionEvent event) {
        String selectedAppointmentDetails = appointmentRequest.getSelectionModel().getSelectedItem();
        if (selectedAppointmentDetails == null) {
            showAlert("Error", "No Appointment Selected", "Please select an appointment before proceeding.");
            return;
        }

        String status = extractStatusFromDetails(selectedAppointmentDetails);
        if ("Accepted".equalsIgnoreCase(status)) {
            openChatWindow(selectedAppointmentDetails);
        } else {
            showAlert("Error", "Unable to Chat", "It's not possible to chat at this moment because the appointment status is " + status + ".");
        }
    }

    private String extractStatusFromDetails(String details) {
        String[] parts = details.split(", ");
        for (String part : parts) {
            if (part.startsWith("Status: ")) {
                return part.substring(8);
            }
        }
        return "";
    }

    private void openChatWindow(String appointmentDetails) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatAppointment.fxml"));
            Parent root = loader.load();
            // Configure your chat controller here
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void changeScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) chat.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




