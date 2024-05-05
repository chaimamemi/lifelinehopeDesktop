package Controllers.user;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnswerService {

    private static final Logger LOGGER = LogManager.getLogger(AnswerService.class);
    private Assistant assistant;

    public void init(SearchAction action) {
        // Log initiation process for debugging, don't append to TextArea
        LOGGER.info("Initiating...");
        initChat(action);
    }

    private void initChat(SearchAction action) {
        StreamingChatLanguageModel model = OpenAiStreamingChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);
        assistant = AiServices.builder(Assistant.class)
                .streamingChatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
        // Log completion of initialization for debugging, don't append to TextArea
        LOGGER.info("Done");
        action.setFinished(); // Only mark the action as finished without appending "Done"
    }

    public void ask(SearchAction action, Button searchButton) {
        LOGGER.info("Asking question '" + action.getQuestion() + "'");
        var responseHandler = new CustomStreamingResponseHandler(action, searchButton);
        assistant.chat(action.getQuestion())
                .onNext(responseHandler::onNext)
                .onComplete(responseHandler::onComplete)
                .onError(responseHandler::onError)
                .start();
    }
}
