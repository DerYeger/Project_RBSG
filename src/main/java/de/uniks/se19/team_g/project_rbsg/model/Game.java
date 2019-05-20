package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;

/**
 * @author Keanu Stückrad
 */
public class Game {

    private String name;
    private int numberOfPlayers;
    private String gameId;

    public Game(@NonNull String name, @NonNull int numberOfPlayers){
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

}
