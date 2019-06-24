package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
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
