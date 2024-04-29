package com.example.sinda.ConnectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private String url = "jdbc:mysql://localhost:3306/lifelinedb";
    private String login = "root";
    private String pwd = "";
    private Connection connection;
    private static DataBase instance;

    private DataBase() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.login, this.pwd);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            // Gérer l'exception de manière appropriée, par exemple, en lançant une exception personnalisée
            throw new RuntimeException(e);
        }
    }

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
