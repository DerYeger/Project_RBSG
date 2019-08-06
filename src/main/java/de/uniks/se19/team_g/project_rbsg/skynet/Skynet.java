package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.Behaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.MovementBehaviour;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class Skynet {

    private final ActionExecutor actionExecutor;
    private final Game game;
    private final Player player;

    //PLACEHOLDER
    private Behaviour currentBehaviour = new MovementBehaviour();

    public Skynet(@NonNull final ActionExecutor actionExecutor,
                  @NonNull final Game game,
                  @NonNull final Player player) {
        this.actionExecutor = actionExecutor;
        this.game = game;
        this.player = player;
    }

    public void turn() {
        final Optional<Action> action = currentBehaviour.apply(game, player);
        if (action.isPresent()) {
            actionExecutor.execute(action.get());
        } else {
            //ignore for now
        }

    }
}
