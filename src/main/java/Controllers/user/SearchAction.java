package Controllers.user;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;

public class SearchAction implements Serializable {
    private final StringProperty timestamp;
    private final StringProperty question;
    private final StringProperty answer;
    private final BooleanProperty finished;
    private final StringProperty dialogue;


    public SearchAction(String question) {
        this.timestamp = new SimpleStringProperty(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty("");
        this.finished = new SimpleBooleanProperty(false);
        this.dialogue = new SimpleStringProperty("");  // Initialize the dialogue with the question
    }

    // Getters and setters
    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty getTimestampProperty() {
        return timestamp;
    }

    public String getQuestion() {
        return question.get();
    }

    public StringProperty getQuestionProperty() {
        return question;
    }

    public String getAnswer() {
        return answer.get();
    }

    public StringProperty getAnswerProperty() {
        return answer;
    }

    public void appendAnswer(String token) {
        this.answer.set(this.answer.get() + token);
        this.dialogue.set(getDialogue() + "\n" + token);  // Append new dialogue
    }

    public boolean isFinished() {
        return finished.get();
    }

    public BooleanProperty getFinishedProperty() {
        return finished;
    }

    public void setFinished(boolean isFinished) {
        this.finished.set(isFinished);
    }

    public String getDialogue() {
        return dialogue.get();
    }

    public void setDialogue(String dialogue) {
        this.dialogue.set(dialogue);
    }

    public StringProperty dialogueProperty() {
        return dialogue;
    }
}
