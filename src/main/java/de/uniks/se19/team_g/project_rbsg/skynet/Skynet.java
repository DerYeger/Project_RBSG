package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.Behaviour;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class Skynet {

    private final ActionExecutor actionExecutor;
    private final Game game;
    private final Player player;

    //PLACEHOLDER
    private Behaviour currentBehaviour;

    public Skynet(@NonNull final ActionExecutor actionExecutor,
                  @NonNull final Game game,
                  @NonNull final Player player) {
        this.actionExecutor = actionExecutor;
        this.game = game;
        this.player = player;
    }

    public Skynet setCurrentBehaviour(@NonNull final Behaviour behaviour) {
        currentBehaviour = behaviour;
        return this;
    }

    public Skynet turn() {
        if (currentBehaviour == null || !game.getCurrentPlayer().equals(player) || game.getPhase().equals("attackPhase")) {
            //ignore for now
            return this;
        }

        final Optional<Action> action = currentBehaviour.apply(game, player);
        if (action.isEmpty()) {
            //ignore for now
        } else if (action.get() instanceof MovementAction) {
            actionExecutor.execute((MovementAction) action.get());
        }

        return this;
    }
}
