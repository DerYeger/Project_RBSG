package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ActionExecutor {

    private final GameEventManager gameEventManager;

    public ActionExecutor(@NonNull final GameEventManager gameEventManager) {
        this.gameEventManager = gameEventManager;
    }

    public void execute(@NonNull final Action action) {
        gameEventManager.sendMessage(action.getServerCommand());
    }
}