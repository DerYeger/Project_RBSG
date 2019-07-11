package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class AutoReadyOnInit implements GameEventHandler {

    private GameEventManager gameEventManager;

    @Override
    public boolean accepts(ObjectNode message) {
        return message.get("action").textValue().equals("gameInitFinished");
    }

    @Override
    public void handle(ObjectNode message) {
        String action = message.get("action").textValue();

        if (action.equals("gameInitFinished")) {
            if (gameEventManager == null) {
                return;
            }
            gameEventManager.sendMessage(CommandBuilder.readyToPlay());
        }
    }

    public void setGameEventManager(GameEventManager gameEventManager) {
        this.gameEventManager = gameEventManager;
    }
}
