package Controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Message;

public class ChatController {

    @FXML
    private TextField messageInput;

    @FXML
    private ListView<Message> messageList;  // Utilisez un objet spécifique si possible

    @FXML
    private Button sendButton;

    @FXML
    void sendchat(ActionEvent event) {
        String messageText = messageInput.getText();
        if (!messageText.isEmpty()) {
            // Simulez l'envoi de message
            Message message = new Message();  // Supposons que vous avez un constructeur approprié
            message.setText(messageText);
            message.setSenderId(62);  // ID de l'utilisateur actuel
            message.setReceiverId(63);  // ID de l'autre utilisateur
            // Ajoutez le message à la liste (simulé ici)
            messageList.getItems().add(message);
            messageInput.clear();
        }
    }

    // Supposons que vous avez une méthode pour charger les messages au démarrage
    public void initialize() {
        loadMessages();
    }

    private void loadMessages() {
        // Ici, chargez les messages depuis le service ou directement
        // Pour le test, ajoutons un message fictif
        Message welcomeMessage = new Message();
        welcomeMessage.setText("Welcome to the chat!");
        welcomeMessage.setSenderId(63);
        messageList.getItems().add(welcomeMessage);
    }
}
