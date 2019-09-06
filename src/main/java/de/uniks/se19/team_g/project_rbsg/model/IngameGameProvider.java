package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.springframework.lang.NonNull;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 * @author Jan Müller
 *
 * provides a .ingame.waiting_room.Game Model that holds the ingame game state with map, players etc
 */
@Component
public class IngameGameProvider {

    private Game game;

    public Game get(){
        if (game == null){

        }
        return game;
    }

    public IngameGameProvider set(@NonNull final Game game) {
        this.game = game;
        return this;
    }

    public IngameGameProvider clear() {
        game = null;
        return this;
    }
}
