package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GameProvider {

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
}
