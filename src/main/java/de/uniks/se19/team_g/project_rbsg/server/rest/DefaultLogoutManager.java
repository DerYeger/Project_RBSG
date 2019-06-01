package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;

/**
 * @author Jan Müller
 */
@Component
public class DefaultLogoutManager implements LogoutManager, Terminable {

    private static final String ENDPOINT = "/user/logout";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserProvider userProvider;

    private final RESTClient restClient;

    @Autowired
    public DefaultLogoutManager(@NonNull final UserProvider userProvider, @NonNull final RESTClient restClient, @NonNull final Terminator terminator) {
        this.userProvider = userProvider;
        this.restClient = restClient;
        terminator.register(this);
    }

    public void logout() {
        if (userProvider.get().getUserKey() != null) {
            final LinkedMultiValueMap<String, String> headers  = new LinkedMultiValueMap<>();
            headers.add("userKey", userProvider.get().getUserKey());

            final String response = restClient.get(ENDPOINT, headers);

            if (logoutSuccessful(response)) {
                logger.debug("Logout successful");
                userProvider.clear();
                WebSocketConfigurator.userKey = null;
            } else {
                logger.debug("Logout unsuccessful");
            }
        } else {
            logger.debug("No user logged in");
        }
    }

    private boolean logoutSuccessful(@NonNull final String response) {
        try {
            final ObjectNode json = new ObjectMapper().readValue(response, ObjectNode.class);
            if (json.has("status")) {
                return json.get("status").asText().equals("success");
            } else {
                logger.debug("Unknown server response");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void terminate() {
        logout();
    }
}
