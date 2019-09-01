package de.uniks.se19.team_g.project_rbsg.server;

import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.ApiClientErrorInterceptor;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.time.Duration;

@Configuration
public class ServerConfig {

    public static final int TIMEOUT_CONNECT = 10;
    public static final int TIMEOUT_READ = 15;

    private ObjectFactory<ApiClientErrorInterceptor> interceptorFactory;

    public ServerConfig(ObjectFactory<ApiClientErrorInterceptor> interceptorFactory) {
        this.interceptorFactory = interceptorFactory;
    }

    @Bean
    public RestTemplate rbsgTemplate(
            @Nonnull UserProvider userProvider
    ) {
        return doBuilldRbsgTemplate(userProvider);
    }

    protected RestTemplate doBuilldRbsgTemplate(@Nonnull UserProvider userProvider) {
        ApiClientErrorInterceptor apiClientErrorInterceptor = interceptorFactory.getObject();
        UserKeyInterceptor userKeyInterceptor = new UserKeyInterceptor(userProvider);

        return new RestTemplateBuilder()
                .additionalInterceptors(userKeyInterceptor, apiClientErrorInterceptor)
                .rootUri("https://rbsg.uniks.de/api")
                .setConnectTimeout(Duration.ofSeconds(TIMEOUT_CONNECT))
                .setReadTimeout(Duration.ofSeconds(TIMEOUT_READ))
                .build();
    }

    public RestTemplate buildTemplateForUser(User user) {
        return doBuilldRbsgTemplate(new UserProvider().set(user));
    }
}
