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

import java.sql.ResultSet;
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

    @FXML
    private Button reloadButton;

    private ObservableList<Message> chatHistory = FXCollections.observableArrayList();
    private int currentUserId = 62;  // Exemple ID de l'utilisateur actuel (docteur ou propriétaire)
    private int otherUserId = 63;  // Exemple ID de l'autre utilisateur
    private Connection cnx;

    public ChatController() {
        // Constructeur vide
    }

    @FXML
    void reloadChat(ActionEvent event) {
        chatHistory.clear();  // Clear the existing messages
        loadMessages();       // Reload messages from the database
        messageList.setItems(chatHistory); // Refresh the ListView with new messages
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
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, currentUserId);
            pstmt.setInt(2, otherUserId);
            pstmt.setInt(3, otherUserId);
            pstmt.setInt(4, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                String message = rs.getString("message");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                Message msg = new Message(id, senderId, receiverId, message, timestamp);
                chatHistory.add(msg);
            }
        } catch (SQLException e) {
            System.out.println("Error loading messages: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void saveMessageToDatabase(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, message, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, message.getSenderId());
            pstmt.setInt(2, message.getReceiverId());
            pstmt.setString(3, message.getMessage());  // Correct column name is 'message'
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(message.getTimestamp()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
            e.printStackTrace();  // Provides a detailed stack trace for diagnosing the issue.
        }
    }

}
