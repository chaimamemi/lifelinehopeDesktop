package Controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import services.AppointmentService;

import java.util.List;

public class AdminVision {

    @FXML
    private ListView<String> DoctorView;
    @FXML
    private ListView<String> ownerView;
    @FXML
    private TextArea reportArea;  // Reference to the TextArea in the FXML

    private final AppointmentService appointmentService = new AppointmentService();

    @FXML
    public void initialize() {
        // Set the TextArea as non-editable
        reportArea.setEditable(false);

        loadDoctorActivities();
        loadOwnerActivities();
        displayReports();  // Automatically display reports on initialization
    }

    private void loadDoctorActivities() {
        List<String> doctorActivities = appointmentService.getDoctorActivities();
        DoctorView.getItems().setAll(doctorActivities);
        DoctorView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });
    }

    private void loadOwnerActivities() {
        List<String> ownerActivities = appointmentService.getOwnerActivities();
        System.out.println("Activities loaded: " + ownerActivities.size());  // Debugging to check how many items are loaded

        if (!ownerActivities.isEmpty()) {
            System.out.println("Sample Activity: " + ownerActivities.get(0));  // Print first activity to verify content
        }

        ownerView.getItems().setAll(ownerActivities);
        ownerView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    System.out.println("Setting text: " + item);  // Debugging to check what's being set
                }
            }
        });
    }


    @FXML
    public void displayReports() {
        int totalAppointments = appointmentService.getTotalAppointmentsCount();
        int cancellationRate = appointmentService.getCancellationRate();
        String peakTimeInfo = appointmentService.getPeakTimeInfo();

        String report = "Total Appointments: " + totalAppointments + "\n" +
                "Cancellation Rate: " + cancellationRate + "%\n" +
                "Peak Times: " + peakTimeInfo;

        reportArea.setText(report);
    }
}
