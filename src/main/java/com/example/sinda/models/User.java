package com.example.sinda.models;

public class User {
    private int id;
    private Integer braceletId; // Utiliser Integer pour gérer les nulls
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Doit être hashé avant d'être stocké
    private String role;

    // Constructeurs, getters et setters
    public User() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public Integer getBraceletId() {
        return braceletId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setBraceletId(Integer braceletId) {
        this.braceletId = braceletId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", braceletId=" + braceletId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}