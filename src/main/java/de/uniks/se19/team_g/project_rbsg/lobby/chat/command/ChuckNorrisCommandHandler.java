package de.uniks.se19.team_g.project_rbsg.lobby.chat.command;

import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import javafx.application.Platform;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ChuckNorrisCommandHandler implements ChatCommandHandler {

    final String uri = "https://api.chucknorris.io/jokes/random";

    public static final String COMMAND = "chuckMe";

    public static final String OPTION_ERROR_MESSAGE = "/chuckMe - Gets you a fine Chuck Norris joke";

    private final ChatController chatController;

    private RestTemplate restTemplate = new RestTemplate();


    public ChuckNorrisCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }


    @Override
    public void handleCommand(@NonNull final ChatChannelController callback, @Nullable final String options) throws Exception {

        final CompletableFuture<HashMap<String, Object>> answer = getChuckNorrisJoke();
        answer
                .thenAccept(joke -> Platform.runLater(() -> jokeReturned((String)joke.get("value"), callback)))
                .exceptionally(exception ->  {
                    callback.displayMessage(ChatController.SYSTEM, exception.getMessage());
                    return null;
                });

    }

    public CompletableFuture<HashMap<String, Object>> getChuckNorrisJoke(){
        return CompletableFuture.supplyAsync(() -> this.restTemplate.getForObject(this.uri, HashMap.class, ""));
    }

    private void jokeReturned(String joke, @NonNull final ChatChannelController callback) {
        chatController.sendMessage(callback, ChatController.GENERAL_CHANNEL_NAME, joke);
    }
}
