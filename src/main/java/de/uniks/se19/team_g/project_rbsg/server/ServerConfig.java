package de.uniks.se19.team_g.project_rbsg.server;

import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ServerConfig {

    public static final int TIMEOUT_CONNECT = 10;
    public static final int TIMEOUT_READ = 15;

    @Bean
    public RestTemplate rbsgTemplate(
        UserKeyInterceptor userKeyInterceptor
    ) {

        return new RestTemplateBuilder()
                .additionalInterceptors(userKeyInterceptor)
                .rootUri("https://rbsg.uniks.de/api")
                .setConnectTimeout(Duration.ofSeconds(TIMEOUT_CONNECT))
                .setReadTimeout(Duration.ofSeconds(TIMEOUT_READ))
                .build();
    }
}