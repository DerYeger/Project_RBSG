package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoryViewConfig {

    @Bean
    public ActionRenderer actionRenderer(
        ObjectProvider<ActionRenderer> rendererProvider
    ) {
        DelegatingActionRenderer renderer = new DelegatingActionRenderer();
        rendererProvider.forEach(renderer::addRenderer);

        return renderer;
    }
}
