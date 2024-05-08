package Controllers.user;

import connectionDB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Message;

import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatController {

    @FXML
    private TextField messageInput;
    @FXML
    private ListView<Message> messageList;
    @FXML
    private Button sendButton;

    private ObservableList<Message> chatHistory = FXCollections.observableArrayList();
    private int currentUserId = 62;  // Exemple ID de l'utilisateur actuel (docteur ou propriétaire)
    private int otherUserId = 63;  // Exemple ID de l'autre utilisateur
    private Connection cnx;

    public ChatController() {
        // Constructeur vide
    }

    @FXML
    public void initialize() {
        // Obtenez la connexion ici
        this.cnx = DatabaseConnector.getInstance().getCnx();
        loadMessages();
        messageList.setItems(chatHistory);
    }

    @FXML
    void sendchat(ActionEvent event) {
        String messageText = messageInput.getText().trim();
        if (!messageText.isEmpty()) {
            Message message = new Message(0, currentUserId, otherUserId, messageText, LocalDateTime.now());
            chatHistory.add(message);
            saveMessageToDatabase(message);
            messageInput.clear();
        }
    }

    private void loadMessages() {
        // Simuler le chargement des messages précédents
        Message welcomeMessage = new Message(0, otherUserId, currentUserId, "Welcome to the chat!", LocalDateTime.now());
        chatHistory.add(welcomeMessage);
    }

    private void saveMessageToDatabase(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, text, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, message.getSenderId());
            pstmt.setInt(2, message.getReceiverId());
            pstmt.setString(3, message.getText());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(message.getTimestamp()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement du message : " + e.getMessage());
        }
    }
}
