package de.uniks.se19.team_g.project_rbsg.server.rest.config;

import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;

@Component
public class UserKeyInterceptor implements ClientHttpRequestInterceptor {

    final UserProvider user;

    public UserKeyInterceptor(@Nonnull UserProvider user) {
        this.user = user;
    }

    @Nonnull
    @Override
    public ClientHttpResponse intercept(@Nonnull HttpRequest request, @Nonnull byte[] body, @Nonnull ClientHttpRequestExecution execution) throws IOException {

        final String userKey = user.get().getUserKey();

        if (userKey != null) {
            request.getHeaders().set("userKey", userKey);
        }

        return execution.execute(request, body);
    }
}
