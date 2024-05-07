package Controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Appointment;
import services.AppointmentService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class CalendarController implements Initializable {
    @FXML
    private FlowPane calendar;
    @FXML
    private Button prevMonthButton;
    @FXML
    private Button nextMonthButton;
    @FXML
    private Text monthLabel;
    @FXML
    private Text yearLabel;
    @FXML
    private Button backaff;

    private LocalDate currentMonth;
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentMonth = LocalDate.now();
        updateCalendar();
        updateMonthYearDisplay();
    }

    @FXML
    private void goToPreviousMonth(ActionEvent event) {
        currentMonth = currentMonth.minusMonths(1);
        updateCalendar();
        updateMonthYearDisplay();
    }

    @FXML
    private void goToNextMonth(ActionEvent event) {
        currentMonth = currentMonth.plusMonths(1);
        updateCalendar();
        updateMonthYearDisplay();
    }

    private void updateMonthYearDisplay() {
        monthLabel.setText(currentMonth.getMonth().toString());
        yearLabel.setText(Integer.toString(currentMonth.getYear()));
    }

    private void updateCalendar() {
        calendar.getChildren().clear();
        YearMonth yearMonth = YearMonth.from(currentMonth);
        LocalDate firstDayOfMonth = currentMonth.withDayOfMonth(1);
        int firstDayOfWeekIndex = firstDayOfMonth.getDayOfWeek().getValue() - 1; // Adjust for zero-based index

        // Create empty cells at the start of the month
        for (int i = 0; i < firstDayOfWeekIndex; i++) {
            calendar.getChildren().add(new StackPane());
        }

        // Populate the calendar with day boxes
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);
            VBox dayBox = createDateBox(date);
            calendar.getChildren().add(dayBox);
        }
    }

    private VBox createDateBox(LocalDate date) {
        VBox dayBox = new VBox(5); // Espacement entre les éléments
        dayBox.getStyleClass().add("calendar-day-box");
        dayBox.setPrefSize(100, 100); // Taille préférée pour tous les boxes
        dayBox.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE); // Assure que la taille ne change pas

        // Label pour le jour et le numéro (e.g., "Monday 29")
        Label dayLabel = new Label(String.format("%s %d", date.getDayOfWeek(), date.getDayOfMonth()));
        dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        dayLabel.setWrapText(true); // Permet au texte de revenir à la ligne si nécessaire
        dayBox.getChildren().add(dayLabel);

        // Ajoute des rendez-vous pour le jour
        List<Appointment> appointments = appointmentService.getAppointmentsForDay(date);
        for (Appointment appt : appointments) {
            String doctorEmail = appointmentService.getDoctorEmailById(appt.getDoctorId());
            Label statusLabel = new Label(String.format("%s - %s", appt.getStatus(), doctorEmail));
            statusLabel.setStyle("-fx-font-size: 10px;");
            statusLabel.setTextFill(getStatusColor(appt.getStatus()));
            statusLabel.setWrapText(true); // Gère le débordement de texte
            dayBox.getChildren().add(statusLabel);
        }

        return dayBox;
    }

    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "accepted":
                return Color.GREEN;
            case "pending":
                return Color.ORANGE;
            case "refused":
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }


    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficheAppointement.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) backaff.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
