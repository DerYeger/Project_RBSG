package de.uniks.se19.team_g.project_rbsg.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class ChatChannelController {

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField inputField;

    private ChatController chatController;

    private String channel;

    public void init(@NonNull final ChatController chatController, @NonNull final String channel) {
        this.chatController = chatController;
        this.channel = channel;

        messageArea.setEditable(false);

        //do not remove
        chatController.registerChatChannelController(this, channel);

        addEventHandler();
    }

    private void addEventHandler() {
        inputField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                handleInput();
            }
        });
    }

    public void displayMessage(@NonNull final String from, @NonNull final String content) {
        Platform.runLater(() -> messageArea.appendText(from + ": " + content + '\n'));
    }

    private void handleInput() {
        if (!inputField.getText().isBlank()) {
            try {
                chatController.handleInput(this, channel, inputField.getText());
            } catch (final Exception e) {
                displayMessage(ChatController.SYSTEM, "An error occurred processing your message");
                e.printStackTrace();
            }
            Platform.runLater(() ->  inputField.clear());
        }
    }
}
