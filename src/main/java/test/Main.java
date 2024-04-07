package test;

import connectionDB.DatabaseConnector;
import models.Appointment;
import models.User;
import services.AppointmentService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Connection connection = DatabaseConnector.getInstance().getCnx();
        if (connection != null) {
            System.out.println("La connexion à la base de données a réussi !");

            User owner = new User();
            owner.setId(1); // ID d'un utilisateur existant avec le rôle OWNER
            owner.setRole("ROLE_OWNER");

            AppointmentService appointmentService = new AppointmentService();

            // Exemple d'ajout d'un rendez-vous
            Appointment newAppointment = new Appointment();
            newAppointment.setPatientId(62); // ID d'un patient existant
            newAppointment.setDateTime(LocalDateTime.now().plusDays(1));
            newAppointment.setDescription("Consultation générale");
            newAppointment.setStatus("Pending");
            newAppointment.setDoctorId(63); // ID d'un docteur existant

            appointmentService.add(newAppointment, owner);

            // Exemple d'affichage de tous les rendez-vous
            //List<Appointment> appointments = appointmentService.getAll(owner);
            //for (Appointment appointment : appointments) {
            //  System.out.println(appointment);
            //   }

            try {
                connection.close();
                System.out.println("La connexion a été fermée avec succès.");
            } catch (Exception e) {
                System.out.println("Échec de la fermeture de la connexion.");
                e.printStackTrace();
            }
        } else {
            System.out.println("La connexion à la base de données a échoué.");
        }
    }
}
