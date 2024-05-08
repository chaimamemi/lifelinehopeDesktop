package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private int id;
    private int senderId;
    private int receiverId;
    private String message;
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(int id, int senderId, int receiverId, String message, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String text) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return  message

                ;
    }

    public List<Message> getMessagesForConversation(int senderId, int receiverId) {
        List<Message> messages = new ArrayList<>();
        // Code pour exécuter une requête SQL qui récupère tous les messages où
        // senderId et receiverId correspondent aux ID passés en paramètre
        return messages;
    }

}
