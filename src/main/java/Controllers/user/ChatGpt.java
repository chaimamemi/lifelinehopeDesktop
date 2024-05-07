package Controllers.user;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChatGpt extends Application {

    @FXML
    private TextField ask;
    @FXML
    private ListView<SearchAction> history;  // Use SearchAction for history
    @FXML
    private TextArea chatanswer;
    @FXML
    private Button searchButton;
    @FXML
    private Button back;

    private final AnswerService answerService = new AnswerService();
    private List<SearchAction> searchHistory;  // To store the history of searches

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void initialize() {
        // Load history from file
        searchHistory = HistoryManager.loadHistory();
        history.getItems().setAll(searchHistory);

        back.setOnAction(this::back);
        history.setCellFactory(param -> new ListCell<SearchAction>() {
            @Override
            protected void updateItem(SearchAction item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTimestamp() + ": " + item.getQuestion());
                }
            }
        });

        history.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !history.getSelectionModel().isEmpty()) {
                SearchAction selectedAction = history.getSelectionModel().getSelectedItem();
                if (chatanswer.textProperty().isBound()) {
                    chatanswer.textProperty().unbind();
                }
                chatanswer.setText(selectedAction.getAnswer());
                ask.setText(selectedAction.getQuestion());
            }
        });

        ask.setOnKeyPressed(this::handleEnterPressed);
    }

    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            search();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ChatGpt.fxml"));
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setTitle("ChatGpt App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void search() {
        String question = ask.getText().trim();
        if (!question.isEmpty()) {
            searchButton.setDisable(true);
            SearchAction searchAction = new SearchAction(question);
            chatanswer.textProperty().unbind();
            chatanswer.textProperty().bind(searchAction.getAnswerProperty());
            answerService.init(searchAction);
            answerService.ask(searchAction, searchButton);
            searchHistory.add(searchAction);
            history.getItems().setAll(searchHistory);
            ask.setText("");  // Clear the input field after submitting
        } else {
            chatanswer.setText("Please enter a question.");
        }
    }

    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AffichageBracelet.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Save history to file on close
        HistoryManager.saveHistory(searchHistory);
    }
}
