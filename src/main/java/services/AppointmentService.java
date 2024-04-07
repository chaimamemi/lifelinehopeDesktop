package services;

import interfaces.IService;
import models.Appointment;
import models.User;
import connectionDB.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService implements IService<Appointment> {

    private final Connection cnx;

    public AppointmentService() {
        this.cnx = DatabaseConnector.getInstance().getCnx();
    }


    @Override
    public void add(Appointment appointment, User user) {
        if (!user.getRole().equals("ROLE_OWNER")) {
            System.out.println("Only owners can create appointments.");
            return;
        }
        String sql = "INSERT INTO appointment (patient_id, date_time, description, status, doctor_id) VALUES (?, ?, ?, 'Pending', ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setTimestamp(2, Timestamp.valueOf(appointment.getDateTime()));
            pstmt.setString(3, appointment.getDescription());
            pstmt.setInt(4, appointment.getDoctorId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Appointment> getAll(Appointment appointment, User user) {
        if (!user.getRole().equals("OWNER")) {
            System.out.println("Only owners can view all appointments.");
            return new ArrayList<>();
        }
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(mapToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public void update(Appointment appointment, User user) {
        if (!user.getRole().equals("OWNER")) {
            System.out.println("Only owners can update appointments.");
            return;
        }
        String sql = "UPDATE appointment SET patient_id = ?, date_time = ?, description = ?, status = ?, doctor_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setTimestamp(2, Timestamp.valueOf(appointment.getDateTime()));
            pstmt.setString(3, appointment.getDescription());
            pstmt.setString(4, appointment.getStatus());
            pstmt.setInt(5, appointment.getDoctorId());
            pstmt.setInt(6, appointment.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Appointment appointment, User user) {
        if (!user.getRole().equals("OWNER")) {
            System.out.println("Only owners can delete appointments.");
            return false;
        }
        String sql = "DELETE FROM appointment WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Utility method to map ResultSet to Appointment object
    private Appointment mapToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        appointment.setDescription(rs.getString("description"));
        appointment.setStatus(rs.getString("status"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        return appointment;
    }


}
