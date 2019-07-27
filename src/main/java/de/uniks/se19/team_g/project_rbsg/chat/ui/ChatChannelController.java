package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.springframework.lang.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import static de.uniks.se19.team_g.project_rbsg.chat.ChatClient.SYSTEM;

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

    private boolean displayTimestamps;

    private ChatTabController tabController;

    public void init(@NonNull final ChatController chatController, @NonNull final String channel) {
        init(chatController, channel, true);
    }

    public void init(@NonNull final ChatController chatController, @NonNull final String channel, final boolean displayTimestamps) {
        this.chatController = chatController;
        this.channel = channel;
        this.displayTimestamps = displayTimestamps;

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

    public void registerTabController(@NonNull final ChatTabController tabController) {
        this.tabController = tabController;
    }

    public void displayMessage(@NonNull final String from, @NonNull final String content) {
        tabController.markUnread();
        Platform.runLater(() -> messageArea.appendText(buildMessage(from, content)));
    }

    private String buildMessage(@NonNull final String from, @NonNull final String content) {
        final StringBuilder sb = new StringBuilder();

        if (displayTimestamps) {
            sb.append(getTimestamp());
        }

        sb.append(' ')
                .append(from)
                .append(": ")
                .append(content)
                .append('\n');

        return sb.toString();
    }

    private String getTimestamp() {
        return '[' + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ']';
    }

    private void handleInput() {
        if (!inputField.getText().isBlank()) {
            try {
                chatController.handleInput(this, channel, inputField.getText());
            } catch (final Exception e) {
                displayMessage(SYSTEM, "An error occurred processing your message");
                e.printStackTrace();
            }
            Platform.runLater(() ->  inputField.clear());
        }
    }

    public void disableInput() {
        inputField.setDisable(true);
    }

    public String getChannel() {
        return channel;
    }


    public TextField getInputField() {
        return inputField;
    }

}
