package Controllers.user;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatGpt extends Application {

    @FXML
    private TextField ask;

    @FXML
    private TextArea chatanswer;

    @FXML
    private Button searchButton;

    @FXML
    private Button back;


    private final AnswerService answerService = new AnswerService();

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void initialize() {

        back.setOnAction(this::back);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ChatGpt.fxml"));
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ChatGpt App");
        primaryStage.show();
    }

    @FXML
    private void search() {
        String question = ask.getText().trim();
        if (!question.isEmpty()) {
            searchButton.setDisable(true);
            SearchAction searchAction = new SearchAction(question);
            chatanswer.textProperty().bind(searchAction.getAnswerProperty());
            answerService.init(searchAction);
            answerService.ask(searchAction, searchButton); // Pass the button here
        } else {
            chatanswer.setText("Please enter a question.");
        }
    }

    @FXML
    void back(ActionEvent event) {

        // Make sure the resource path is correct and the FXML file is located in the resources folder.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageBracelet.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) back.getScene().getWindow(); // Get the current button's window
            stage.setScene(scene); // Set the newly loaded scene for the window
            stage.show(); // Show the window with the new scene
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging.
            // Here you can handle the exception, like showing an alert to the user.
        }
    }
}
