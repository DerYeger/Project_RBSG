package de.uniks.se19.team_g.project_rbsg.lobby.chat.command;

import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ChuckNorrisCommandHandler implements ChatCommandHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    final String uri = "https://api.chucknorris.io/jokes/random";

    public static final String COMMAND = "chuckMe";

    public static final String ERROR_MESSAGE = "Error - Chuck Norris is to funny for you";

    private final ChatController chatController;

    private RestTemplate restTemplate;


    public ChuckNorrisCommandHandler(@NonNull final ChatController chatController, @NonNull RestTemplate restTemplate) {
        this.chatController = chatController;
        this.restTemplate = restTemplate;
    }


    @Override
    public void handleCommand(@NonNull final ChatChannelController callback, @Nullable final String options) throws Exception {

        final CompletableFuture<HashMap<String, Object>> answer = getChuckNorrisJoke();
        answer
                .thenAccept(joke -> jokeReturned((String)joke.get("value"), callback))
                .exceptionally(exception ->  {
                    callback.displayMessage(ChatController.SYSTEM, ERROR_MESSAGE);
                    logger.debug(exception.getMessage());
                    return null;
                });

    }

    public CompletableFuture<HashMap<String, Object>> getChuckNorrisJoke(){
        return CompletableFuture.supplyAsync(() -> this.restTemplate.getForObject(this.uri, HashMap.class, ""));
    }

    private void jokeReturned(String joke, @NonNull final ChatChannelController callback) {
        chatController.sendMessage(callback, callback.getChannel(), joke);
    }
}