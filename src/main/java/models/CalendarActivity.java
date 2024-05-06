package models;

import java.time.LocalDate;

public class CalendarActivity {
    private LocalDate date;
    private String doctorEmail;

    public CalendarActivity(LocalDate date, String doctorEmail) {
        this.date = date;
        this.doctorEmail = doctorEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }
}