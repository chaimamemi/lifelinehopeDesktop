package Controllers.user;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomStreamingResponseHandler {

    private static final Logger LOGGER = LogManager.getLogger(CustomStreamingResponseHandler.class);
    private final SearchAction action;
    private final Button searchButton; // Reference to the search button

    public CustomStreamingResponseHandler(SearchAction action, Button searchButton) {
        this.action = action;
        this.searchButton = searchButton; // Set the search button
    }

    public void onNext(String token) {
        Platform.runLater(() -> action.appendAnswer(token));
    }

    public void onComplete(Response<AiMessage> response) {
        Platform.runLater(() -> {
            LOGGER.info("Complete response: " + response.toString());
            LOGGER.info("Answer is complete for '" + action.getQuestion() + "', size: " + action.getAnswer().length());
            action.setFinished();
            searchButton.setDisable(false); // Re-enable the search button here
        });
    }

    public void onError(Throwable error) {
        Platform.runLater(() -> {
            LOGGER.error("Error while receiving answer: " + error.getMessage());
            action.appendAnswer("\nSomething went wrong: " + error.getMessage());
            action.setFinished();
            searchButton.setDisable(false); // Re-enable the search button here as well
        });
    }
}
