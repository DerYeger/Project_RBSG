package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoryViewConfig {

    @Bean
    public ActionRenderer actionRenderer(

    ) {
        return new MovementActionRenderer();
    }
}
