package org.example.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static DatabaseConnector instance ;

    private final String URL="jdbc:mysql://127.0.0.1:3306/lifelinehope";
    private final String USERNAME ="root";
    private final String PASSWORD ="";
    private Connection cnx ;

    private DatabaseConnector(){

        try {
            cnx = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("  " +
                    "Connected ... ");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(" ___ Connection Failed ___");
        }
    }


    public static DatabaseConnector getInstance() {
        if(instance == null)
            instance = new DatabaseConnector();

        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}






