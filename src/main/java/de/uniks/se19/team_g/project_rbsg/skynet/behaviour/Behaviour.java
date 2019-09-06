package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
import org.springframework.lang.NonNull;

import java.util.Optional;

@FunctionalInterface
public interface Behaviour {

    Optional<? extends Action> apply(@NonNull final Game game, @NonNull final Player player);

    default int getPriority() {return 0;}
}
