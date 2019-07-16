package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 * @author Jan Müller
 *
 * provides a .model.Game Model that holds the lobby game knowledge like neededPlayerCount
 */
@Component
public class GameProvider implements Terminable {

    private Game game;

    public Game get(){
        if (game == null){

        }
        return game;
    }

    public GameProvider set(@NonNull final Game game) {
        this.game = game;
        return this;
    }

    public GameProvider clear() {
        game = null;
        return this;
    }

    @Override
    public void terminate() {
        clear();
    }
}
