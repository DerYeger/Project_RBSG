package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import org.springframework.lang.NonNull;

public class PassAction implements Action {

    public final Game game;

    public PassAction(@NonNull final Game game) {
        this.game = game;
    }
}
