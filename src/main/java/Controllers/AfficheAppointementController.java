package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Appointment;
import services.AppointmentService;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;




public class AfficheAppointementController {

    @FXML
    private ListView<String> appointmentRequest; // ListView pour afficher les rendez-vous

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Button update;



    private final AppointmentService appointmentService = new AppointmentService(); // Service pour gérer les rendez-vous

    @FXML
    private void initialize() {
        displayAppointments(); // Appeler la méthode pour afficher les rendez-vous au démarrage du contrôleur
        add.setOnAction(this::addapp);
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
                        // Sépare le label du contenu
                        String[] labelAndContent = part.split(": ", 2);
                        Text labelText = new Text(labelAndContent[0] + ": ");
                        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                        Text contentText = new Text(labelAndContent.length > 1 ? labelAndContent[1] : "");
                        contentText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                        // Condition spécifique pour la couleur du statut
                        if (labelAndContent[0].equals("Status")) {
                            String statusValue = labelAndContent[1].trim();
                            if ("Accepted".equalsIgnoreCase(statusValue)) {
                                contentText.setFill(Color.GREEN);
                            } else {
                                contentText.setFill(Color.RED);
                            }
                        } else {
                            // Pour les autres valeurs, on applique le bleu
                            contentText.setFill(Color.BLUE);
                        }

                        // Ajouter les Text au TextFlow
                        textFlow.getChildren().addAll(labelText, contentText);

                        // Séparateur après chaque partie
                        Text separator = new Text(", ");
                        separator.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                        textFlow.getChildren().add(separator);
                    }

                    // Enlève le dernier séparateur
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

            String appointmentInfo = String.format("Name Patient: %s, Date & Time: %s, Doctor Email: %s, Status: %s",
                    patientName, appointment.getDateTime(), doctorEmail, status);

            appointmentRequest.getItems().add(appointmentInfo);
        }
    }




    @FXML
    void addapp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddApoitment.fxml"));
        try {
            Parent root = loader.load();
            AddAppointmentController  controller = loader.getController();
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

    }

    @FXML
    void updateapp(ActionEvent event) {



    }



}