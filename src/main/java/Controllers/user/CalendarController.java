package Controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
        int firstDayOfWeekIndex = firstDayOfMonth.getDayOfWeek().getValue() - 1;  // Adjust for zero-based index

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
        VBox dayBox = new VBox();
        dayBox.getStyleClass().add("calendar-day-box");  // Apply CSS styling
        dayBox.setPrefSize(126, 118);  // Set the preferred width and height

        // Day number and day of week
        Text dayNum = new Text(date.getDayOfMonth() + " - " + date.getDayOfWeek().toString());
        dayBox.getChildren().add(dayNum);

        // Retrieve appointments for the date
        List<Appointment> appointments = appointmentService.getAppointmentsForDay(date);
        for (Appointment appt : appointments) {
            // Fetching the doctor's email
            String doctorEmail = appointmentService.getDoctorEmailById(appt.getDoctorId());

            Text statusText = new Text(appt.getStatus() + " - " + doctorEmail);  // Including doctor's email in the status text
            switch (appt.getStatus().toLowerCase()) {
                case "accepted":
                    statusText.setFill(Color.GREEN);
                    dayBox.setStyle("-fx-background-color: #90EE90;");  // Light green for accepted
                    break;
                case "pending":
                    statusText.setFill(Color.YELLOW);
                    dayBox.setStyle("-fx-background-color: #4e5036;");  // Gold for pending
                    break;
                case "refused":
                    statusText.setFill(Color.RED);
                    dayBox.setStyle("-fx-background-color: #F08080;");  // Light red for refused
                    break;
                default:
                    dayBox.setStyle("-fx-background-color: #97cffa;");  // Default light blue
                    break;
            }
            dayBox.getChildren().add(statusText);
        }

        return dayBox;
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
