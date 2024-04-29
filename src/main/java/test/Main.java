package test;

import connectionDB.DatabaseConnector;
import models.Appointment;
import models.User;
import services.AppointmentService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import models.Bracelet;
import services.BraceletService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the appointment system!");

        // Créez une instance de AppointmentService
        AppointmentService appointmentService = new AppointmentService();


        // Créer une instance de BraceletService
        BraceletService braceletService = new BraceletService();





        // Créez un ScheduledExecutorService pour planifier des tâches avec une nouvelle connexion à chaque exécution
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Planifiez la méthode de vérification pour s'exécuter toutes les 10 minutes, par exemple
        executorService.scheduleAtFixedRate(() -> {
            // Essayez de créer une nouvelle connexion pour chaque opération planifiée
            try (Connection connection = DatabaseConnector.getInstance().getCnx()) {
                if (connection != null) {
                    System.out.println("La connexion à la base de données est ouverte pour la tâche planifiée.");

                    // Logique métier, comme ajouter des rendez-vous, notifier les docteurs, etc.
                    User owner = new User();
                    owner.setId(1);
                    owner.setRole("ROLE_OWNER");



                    Appointment newAppointment = new Appointment();
                    newAppointment.setPatientId(62);
                    newAppointment.setDateTime(LocalDateTime.now().plusDays(1));
                    newAppointment.setDescription("Consultation");
                    newAppointment.setStatus("Pending");
                    newAppointment.setDoctorId(63);

                    appointmentService.add(newAppointment, owner);
                    int id = appointmentService.getPatientIdByUserName("chaima");
                    System.out.println(id);


                    User doctor = new User();
                    doctor.setId(63);
                    doctor.setRole("ROLE_DOCTOR");

                    appointmentService.notifyDoctor(22, doctor);

                    List<Appointment> appointments = appointmentService.getAll(newAppointment, owner);
                    appointments.forEach(System.out::println);

                    // Récupérer tous les bracelets et les afficher
                    List<Bracelet> bracelets = braceletService.getAllBracelets();
                    for (Bracelet bracelet : bracelets) {
                        System.out.println(bracelet);
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de l'exécution de la tâche planifiée.");
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.MINUTES);

        System.out.println("Tâche de vérification des rendez-vous planifiée pour s'exécuter toutes les 10 minutes.");

    }
}
