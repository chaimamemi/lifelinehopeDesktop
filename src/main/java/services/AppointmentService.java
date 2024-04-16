package services;

import interfaces.IService;
import models.Appointment;
import models.User;
import connectionDB.DatabaseConnector;

import java.sql.*;
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
        if (!user.getRole().equals("ROLE_OWNER")) {
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
        if (!user.getRole().equals("ROLE_OWNER")) {
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
        if (!user.getRole().equals("ROLE_OWNER")) {
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



    public void notifyDoctor(int appointmentId, User doctor) {
        if (!doctor.getRole().equals("ROLE_DOCTOR")) {
            System.out.println("Seuls les docteurs peuvent recevoir des notifications de rendez-vous.");
            return;
        }
        String sql = "SELECT * FROM appointment WHERE id = ? AND doctor_id = ? AND status = 'Pending'";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setInt(2, doctor.getId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Le docteur a été notifié de la demande de rendez-vous en attente.
                System.out.println("Le docteur a été notifié de la demande de rendez-vous.");
            } else {
                System.out.println("Aucun rendez-vous en attente trouvé pour ce docteur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void respondToAppointment(int appointmentId, User doctor, String response) {
        if (!doctor.getRole().equals("ROLE_DOCTOR")) {
            System.out.println("Seuls les docteurs peuvent répondre aux rendez-vous.");
            return;
        }
        if (!response.equals("Accepted") && !response.equals("Refused")) {
            System.out.println("Réponse invalide. Les options sont 'Accepted' ou 'Refused'.");
            return;
        }
        String sql = "UPDATE appointment SET status = ? WHERE id = ? AND doctor_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, response);
            pstmt.setInt(2, appointmentId);
            pstmt.setInt(3, doctor.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le statut du rendez-vous a été mis à jour en: " + response);
            } else {
                System.out.println("Échec de la mise à jour du statut du rendez-vous ou le rendez-vous n'existe pas.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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







    //autres metiers
    @Override
    public void confirmAppointment(int appointmentId, User patient) {
        String sql = "UPDATE appointment SET is_confirmed = TRUE WHERE id = ? AND patient_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setInt(2, patient.getId()); // Assurez-vous que le patient a un ID
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le rendez-vous avec l'ID " + appointmentId + " a été confirmé par le patient avec l'ID " + patient.getId() + ".");
            } else {
                System.out.println("Échec de la confirmation du rendez-vous. Assurez-vous que le rendez-vous existe et correspond au patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void markAppointmentAsUrgent(int appointmentId) {
        String sql = "UPDATE appointment SET is_urgent = TRUE WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le rendez-vous avec l'ID " + appointmentId + " a été marqué comme urgent.");
            } else {
                System.out.println("Échec de marquer le rendez-vous comme urgent. Assurez-vous que le rendez-vous existe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public List<Appointment> getAllPendingAppointments() {
        List<Appointment> pendingAppointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE status = 'Pending'";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pendingAppointments.add(mapToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingAppointments;
    }


    }









