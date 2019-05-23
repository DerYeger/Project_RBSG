package de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Lobby
{
    private SystemMessageManager systemMessageManager;
    private ObservableList<Game> games;
    private ObservableList<Player> players;

    public SystemMessageManager getSystemMessageManager() {
        return systemMessageManager;
    }

    public void setSystemMessageManager(SystemMessageManager systemMessageManager) {
        this.systemMessageManager = systemMessageManager;
    }

    public Lobby()
    {
        games = FXCollections.observableArrayList();
        players = FXCollections.observableArrayList();
    }


    public ObservableList<Game> getGames()
    {
        return games;
    }

    public void setGames(ObservableList<Game> games)
    {
        this.games = games;
    }

    public ObservableList<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(ObservableList<Player> players)
    {
        this.players = players;
    }
}
