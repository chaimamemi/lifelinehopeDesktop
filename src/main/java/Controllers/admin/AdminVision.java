package Controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import services.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

public class AdminVision {

    @FXML
    private ListView<String> DoctorView;
    @FXML
    private ListView<String> ownerView;
    @FXML
    private TextArea reportArea;  // Reference to the TextArea in the FXML
    @FXML
    private TextField searchDoctorfx;   // TextField for doctor search input
    @FXML
    private TextField searchOwnerfx;    // TextField for owner search input

    private final AppointmentService appointmentService = new AppointmentService();
    private ObservableList<String> doctorActivitiesList;
    private ObservableList<String> ownerActivitiesList;

    @FXML
    public void initialize() {
        reportArea.setEditable(false);

        loadDoctorActivities();
        loadOwnerActivities();
        displayReports();

        setupDoctorSearchListener();
        setupOwnerSearchListener();
    }

    private void loadDoctorActivities() {
        List<String> doctorActivities = appointmentService.getDoctorActivities();
        doctorActivitiesList = FXCollections.observableArrayList(doctorActivities);
        DoctorView.getItems().setAll(doctorActivitiesList);
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
        ownerActivitiesList = FXCollections.observableArrayList(ownerActivities);
        ownerView.getItems().setAll(ownerActivitiesList);
        ownerView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });
    }

    private void setupDoctorSearchListener() {
        searchDoctorfx.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDoctors(newValue);
        });
    }

    private void setupOwnerSearchListener() {
        searchOwnerfx.textProperty().addListener((observable, oldValue, newValue) -> {
            filterOwners(newValue);
        });
    }

    private void filterDoctors(String filter) {
        if (filter == null || filter.isEmpty()) {
            DoctorView.getItems().setAll(doctorActivitiesList);
        } else {
            List<String> filteredDoctors = doctorActivitiesList.stream()
                    .filter(activity -> activity.toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            DoctorView.getItems().setAll(filteredDoctors);
        }
    }

    private void filterOwners(String filter) {
        if (filter == null || filter.isEmpty()) {
            ownerView.getItems().setAll(ownerActivitiesList);
        } else {
            List<String> filteredOwners = ownerActivitiesList.stream()
                    .filter(activity -> activity.toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            ownerView.getItems().setAll(filteredOwners);
        }
    }

    @FXML
    public void displayReports() {
        int totalAppointments = appointmentService.getTotalAppointmentsCount();
        int cancellationRate = appointmentService.getCancellationRate();
        String peakTimeInfo = appointmentService.getPeakTimeInfo();

        String report = String.format("Total Appointments: %d\nCancellation Rate: %d%%\nPeak Times: %s",
                totalAppointments, cancellationRate, peakTimeInfo);
        reportArea.setText(report);
    }
}
