package de.uniks.se19.team_g.project_rbsg.ingame;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventHandler;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;

public class GameInitFinishedListener implements GameEventHandler {

    private final IngameContext ingameContext;
    private boolean fired = false;

    public GameInitFinishedListener(IngameContext ingameContext) {
        this.ingameContext = ingameContext;
    }

    @Override
    public boolean accepts(ObjectNode message) {
        if (fired) return false;
        return GameEventManager.isActionType(message, GameEventManager.GAME_INIT_FINISHED);
    }

    @Override
    public void handle(ObjectNode message) {
        ingameContext.getModelManager().getExecutor().execute(
                () -> ingameContext.gameInitialized(ingameContext.getModelManager().getGame())
        );
        fired = true;
    }
}
