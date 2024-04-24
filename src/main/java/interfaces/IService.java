package interfaces;

import java.util.List;

import models.Appointment;
import models.User; // Assurez-vous que le chemin d'accès est correct pour User

public interface IService<T> {
    void add(T t, User user); // Ajout de User en tant que paramètre
    List<T> getAll(T t, User user);
    void update(T t, User user); // Ajout de User en tant que paramètre
    boolean delete(T t, User user); // Ajout de User en tant que paramètre

    void notifyDoctor(int appointmentId, User doctor); //Ajout de methode pour assurer que le doctor peut accpetr ou refuser appoitment
    void confirmAppointment(int appointmentId, User patient);
    void markAppointmentAsUrgent(int appointmentId);
    void checkAppointmentsAndNotify() ;



}
