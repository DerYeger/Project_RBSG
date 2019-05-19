package de.uniks.se19.team_g.project_rbsg.model;

/**
 * @author Keanu Stückrad
 */
public class GameBuilder {

    private Game game;

    public Game getGame(String name, int numberOfPlayers){
        if(this.game == null ){
            this.game = new Game(name, numberOfPlayers);
        }
        return this.game;
    }


}
