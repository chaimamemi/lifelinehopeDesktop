package services;

import interfaces.IService;
import models.Appointment;
import models.User;
import connectionDB.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class AppointmentService implements IService<Appointment> {

    private final Connection cnx;

    public AppointmentService() {
        this.cnx = DatabaseConnector.getInstance().getCnx();
    }

    @Override
    public void add(Appointment appointment, User user) {
        if (user == null || user.getRole() == null || !user.getRole().equals("ROLE_OWNER")) {
            System.out.println("Invalid user or user role. Only owners can create appointments.");
            return;
        }

        String sql = "INSERT INTO appointment (patient_id, date_time, description, status, doctor_id, is_confirmed, is_urgent) VALUES (?, ?, ?, 'Pending', ?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setTimestamp(2, Timestamp.valueOf(appointment.getDateTime()));
            pstmt.setString(3, appointment.getDescription());
            pstmt.setInt(4, appointment.getDoctorId());

            // Convertir les valeurs de isConfirmed et isUrgent en entiers (1 si vrai, 0 si faux)
            int isConfirmedValue = appointment.getIsConfirmed() ? 1 : 0;
            int isUrgentValue = appointment.getIsUrgent() ? 1 : 0;

            pstmt.setInt(5, isConfirmedValue);
            pstmt.setInt(6, isUrgentValue);

            pstmt.executeUpdate();
            System.out.println("Appointment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add appointment.");
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
    public boolean update(Appointment appointment, User user) {
        return false;
    }

    public boolean updateAppointment(int appointmentId, String patientName, String description, LocalDate date) throws SQLException {
        int patientId = getPatientIdByUserName(patientName); // Vous devez avoir cette méthode.
        if (patientId == -1) {
            System.out.println("Le nom du patient n'a pas été trouvé.");
            return false;
        }

        // Convertissez LocalDate en LocalDateTime
        LocalDateTime dateTime = date.atStartOfDay();

        // Votre code pour mettre à jour le rendez-vous dans la base de données
        String sql = "UPDATE appointment SET patient_id = ?, date_time = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setTimestamp(2, Timestamp.valueOf(dateTime));
            pstmt.setString(3, description);
            pstmt.setInt(4, appointmentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Une erreur SQL s'est produite lors de la mise à jour du rendez-vous: " + e.getMessage());
            e.printStackTrace();
            return false;
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
                System.out.println("Vous avez une demande de rendez-vous.");
            } else {
                System.out.println("Aucun rendez-vous en attente trouvé pour ce docteur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean respondToAppointment(int appointmentId, String response, User doctor) {
        if (!doctor.getRole().equals("ROLE_DOCTOR")) {
            System.out.println("Seuls les docteurs peuvent répondre aux rendez-vous.");
            return false;
        }

        if (!response.equals("Accepted") && !response.equals("Refused")) {
            System.out.println("Réponse invalide. Les options sont 'Accepted' ou 'Refused'.");
            return false;
        }

        String sql = "UPDATE appointment SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, response);
            pstmt.setInt(2, appointmentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le statut du rendez-vous a été mis à jour en: " + response);
                return true;
            } else {
                System.out.println("Échec de la mise à jour du statut du rendez-vous ou le rendez-vous n'existe pas.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


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


    @Override
    public void confirmAppointment(int appointmentId, User patient) {
        String sql = "UPDATE appointment SET is_confirmed = TRUE WHERE id = ? AND patient_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setInt(2, patient.getId());
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
        String sql = "SELECT * FROM appointment WHERE status = 'Pending' or  status = 'accepted' or  status = 'refused'";
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


    public void checkAppointmentsAndNotify() {
        List<Appointment> pendingAppointments = getAllPendingAppointments();

        for (Appointment appt : pendingAppointments) {
            if (appt.getStatus().equals("Accepted")) {
                System.out.println("Notification: Doctor ID " + appt.getDoctorId() +
                        " - Your appointment with patient ID " + appt.getPatientId() + " has been accepted.");
            } else if (appt.getStatus().equals("Pending")) {
                System.out.println("Notification: Patient ID " + appt.getPatientId() +
                        " - Your appointment request is still pending.");
            }
        }
    }


    public int getPatientIdByUserName(String patientName) throws SQLException {
        String sql = "SELECT id FROM user WHERE first_name = ? AND role = 'ROLE_OWNER'";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, patientName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int getDoctorIdByEmail(String doctorEmail) {
        String sql = "SELECT id FROM user WHERE email = ? AND role = 'ROLE_DOCTOR'";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, doctorEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getAllDoctorEmails() {
        List<String> doctorEmails = new ArrayList<>();
        String sql = "SELECT email FROM user WHERE role = 'ROLE_DOCTOR'";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String email = rs.getString("email");
                doctorEmails.add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorEmails;
    }


    public String getRoleById(int userId) {
        String role = "";
        String sql = "SELECT role FROM user WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }


    public String getPatientNameById(int patientId) {
        String patientName = "";
        String sql = "SELECT first_name, last_name FROM user WHERE id = ? AND role = 'ROLE_OWNER'";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                patientName = firstName + " " + lastName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientName;
    }

    public String getDoctorEmailById(int doctorId) {
        String doctorEmail = "";
        String sql = "SELECT email FROM user WHERE id = ? AND role = 'ROLE_DOCTOR'";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                doctorEmail = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorEmail;
    }


    public Appointment getAppointmentById(int appointmentId) {
        Appointment appointment = null;
        String sql = "SELECT * FROM appointment WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                appointment = mapToAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }
}