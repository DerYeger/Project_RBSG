package de.uniks.se19.team_g.project_rbsg.server.rest;

import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

/**
 * @author Jan Müller
 */
@Component
public class LogoutManager implements ILogoutManager {

    private static final String ENDPOINT = "/user/logout";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserProvider userProvider;

    private final RESTClient restClient;

    @Autowired
    public LogoutManager(@NonNull final UserProvider userProvider, @NonNull final RESTClient restClient) {
        this.userProvider = userProvider;
        this.restClient = restClient;
    }

    public void logout() {
        if (userProvider.get().getUserKey() != null) {
            final LinkedMultiValueMap<String, String> headers  = new LinkedMultiValueMap<>();
            headers.add("userKey", userProvider.get().getUserKey());

            restClient.get(ENDPOINT, headers);

            logger.debug("User logged out");
        } else {
            logger.debug("No user logged in");
        }
    }
}
