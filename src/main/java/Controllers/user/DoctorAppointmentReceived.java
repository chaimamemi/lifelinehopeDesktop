package Controllers.user;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import javafx.scene.layout.HBox;

public class DoctorAppointmentReceived {

    private final AppointmentService appointmentService = new AppointmentService();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @FXML
    private Button accept;

    @FXML
    private TextField searchfx;

    @FXML
    private ListView<Appointment> appointmentReceived;

    @FXML
    private Button notification;

    @FXML
    private void initialize() {
        List<Appointment> pendingAppointments = appointmentService.getAllPendingAppointments();
        appointmentReceived.getItems().addAll(pendingAppointments);

        appointmentReceived.setCellFactory(listView -> new ListCell<Appointment>() {
            @Override
            protected void updateItem(Appointment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10); // Espace entre les éléments dans HBox
                    TextFlow textFlow = createTextFlow(item);

                    Button chatButton = new Button("Chat");
                    chatButton.setOnAction(e -> openChatWindow(item)); // Assurez-vous que la méthode openChatWindow est définie ailleurs
                    hbox.getChildren().addAll(textFlow, chatButton);
                    setGraphic(hbox);
                }
            }
        });

        scheduler.scheduleAtFixedRate(this::checkForNewAppointments, 0, 1, TimeUnit.MINUTES);
    }

    private void openChatWindow(Appointment appointment) {
        // Logique pour ouvrir la fenêtre de chat
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatAppointment.fxml"));
            Parent root = loader.load();
            ChatController chatController = loader.getController();
            // Configurez ici avec les informations de l'appointment
            Stage stage = new Stage();
            stage.setTitle("Chat with " + appointmentService.getPatientNameById(appointment.getPatientId()));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForNewAppointments() {
        Platform.runLater(() -> {
            List<Appointment> newAppointments = appointmentService.getNewAppointments();
            if (!newAppointments.isEmpty()) {
                appointmentReceived.getItems().addAll(newAppointments);
                showNotification("New Appointment", "You have new appointment requests.");
            }
        });
    }

    private TextFlow createTextFlow(Appointment item) {
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(
                createText("Nom Patient: ", Color.BLUE),
                createText(appointmentService.getPatientNameById(item.getPatientId()) + "\n", Color.BLACK),
                createText("Description: ", Color.BLUE),
                createText(item.getDescription() + "\n", Color.BLACK),
                createText("Date & Heure: ", Color.BLUE),
                createText(item.getDateTime().toString() + "\n", Color.BLACK),
                createText("Urgence: ", Color.BLUE),
                createText(item.getIsUrgent() ? "Urgent" : "Not Urgent" + "\n", Color.BLACK)
        );
        return textFlow;
    }

    private Text createText(String content, Color color) {
        Text text = new Text(content);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setFill(color);
        return text;
    }

    private void showNotification(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(javafx.util.Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
    }

    @FXML
    void respondToAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentReceived.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();

        if (selectedAppointment == null) {
            showAlert("Attention", "No Appointment Selected", "Please select an appointment before proceeding.");
            return;
        }

        Button sourceButton = (Button) event.getSource();
        String statusToUpdate = sourceButton == accept ? "Accepted" : "Refused";

        if (!statusToUpdate.isEmpty()) {
            boolean updated = appointmentService.respondToAppointment(selectedAppointment.getId(), statusToUpdate, currentUser);
            if (updated) {
                selectedAppointment.setStatus(statusToUpdate);
                appointmentReceived.refresh();
                showAlert("Success", "Appointment Updated", "The appointment status has been updated successfully.");
            } else {
                showAlert("Error", "Failed to Update Appointment", "Unable to update the appointment status.");
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
