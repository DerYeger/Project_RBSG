package de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

public class Lobby
{
    private SystemMessageManager systemMessageManager;
    private ObservableList<Game> games;
    private ObservableList<Player> players;

    public Lobby()
    {
        games = FXCollections.observableArrayList();
        players = FXCollections.observableArrayList();
    }

    public SystemMessageManager getSystemMessageManager()
    {
        return systemMessageManager;
    }

    public void setSystemMessageManager(SystemMessageManager systemMessageManager)
    {
        this.systemMessageManager = systemMessageManager;
    }

    public ObservableList<Game> getGames()
    {
        return games;
    }

    public void setGames(ObservableList<Game> games)
    {
        this.games = games;
    }

    public void addGame(Game game)
    {
        if (game != null)
        {
            Platform.runLater(() -> games.add(game));
        }
    }

    public Lobby withGame(Game game)
    {
        addGame(game);
        return this;
    }

    public void addGames(Game... gameList)
    {
        if (gameList != null)
        {
            for (Game game : gameList)
            {
                if (game != null)
                {
                    Platform.runLater(() -> games.add(game));
                }
            }
        }
    }

    public Lobby withGames(Game... gameList)
    {
        addGames(gameList);
        return this;
    }

    public void addAllGames(Collection<Game> gamesList)
    {
        if(games != null) {
            for (Game game: gamesList)
            {
                Platform.runLater(() -> games.add(game));
            }
        }
    }

    public void removeGame(Game game)
    {
        if (game != null && games.contains(game))
        {
            Platform.runLater(() ->games.remove(game));
        }
    }

    public void addPlayer(Player player)
    {
        if (player != null)
        {
            Platform.runLater(() -> players.add(player));
        }
    }

    public Lobby withPlayer(Player player)
    {
        addPlayer(player);
        return this;
    }

    public void addPlayers(Player... playerList)
    {
        if (playerList != null)
        {
            for (Player player : playerList)
            {
                if (player != null)
                {
                    Platform.runLater(() -> players.add(player));
                }
            }
        }
    }

    public Lobby withPlayers(Player... playerList)
    {
        addPlayers(playerList);
        return this;
    }

    public void addAllPlayer(Collection<Player> playerList)
    {
        if(playerList != null) {
            for (Player player: playerList)
            {
                Platform.runLater(() -> players.add(player));
            }
        }
    }

    public void removePlayer(Player player)
    {
        if (player != null && players.contains(player))
        {
            Platform.runLater(() -> players.remove(player));
        }
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
