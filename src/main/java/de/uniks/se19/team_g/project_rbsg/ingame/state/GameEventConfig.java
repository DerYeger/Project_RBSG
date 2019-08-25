package de.uniks.se19.team_g.project_rbsg.ingame.state;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class GameEventConfig {

    @Bean
    public GameEventDispatcher gameEventDispatcher(
            ApplicationContext context
    ) {
        Map<String, GameEventDispatcher.Adapter> adapterMap = context.getBeansOfType(GameEventDispatcher.Adapter.class);
        Map<String, GameEventDispatcher.Listener> listenerMap = context.getBeansOfType(GameEventDispatcher.Listener.class);

        GameEventDispatcher gameEventDispatcher = new GameEventDispatcher();

        adapterMap.forEach((s, adapter) -> gameEventDispatcher.addAdapter(adapter));
        listenerMap.forEach((s, listener) -> gameEventDispatcher.addListener(listener));

        return gameEventDispatcher;
    }
}
