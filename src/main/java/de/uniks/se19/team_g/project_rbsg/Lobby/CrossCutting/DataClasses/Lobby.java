package de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.lang.NonNull;

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

//    public void setGames(final @NonNull ObservableList<Game> games)
//    {
//        this.games = games;
//    }

    public void addGame(final @NonNull Game game)
    {
        Platform.runLater(() -> games.add(game));
    }

    public Lobby withGame(final @NonNull Game game)
    {
        addGame(game);
        return this;
    }

    public void addGames(final @NonNull Game... gameList)
    {
        for (Game game : gameList)
        {
            if (game != null)
            {
                Platform.runLater(() -> games.add(game));
            }
        }
    }

    public Lobby withGames(final @NonNull Game... gameList)
    {
        addGames(gameList);
        return this;
    }

    public void addAllGames(final @NonNull Collection<Game> gamesList)
    {
        for (Game game: gamesList)
        {
            Platform.runLater(() -> games.add(game));
        }
    }

    public void removeGame(final @NonNull Game game)
    {
        if (games.contains(game))
        {
            Platform.runLater(() ->games.remove(game));
        }
    }

    public void addPlayer(final @NonNull Player player)
    {
        Platform.runLater(() -> players.add(player));
    }

    public Lobby withPlayer(final @NonNull Player player)
    {
        addPlayer(player);
        return this;
    }

    public void addPlayers(final @NonNull Player... playerList)
    {
        for (Player player : playerList)
        {
            if (player != null)
            {
                Platform.runLater(() -> players.add(player));
            }
        }
    }

    public Lobby withPlayers(final @NonNull Player... playerList)
    {
        addPlayers(playerList);
        return this;
    }

    public void addAllPlayer(final @NonNull Collection<Player> playerList)
    {
        for (Player player: playerList)
        {
            Platform.runLater(() -> players.add(player));
        }
    }

    public void removePlayer(final @NonNull Player player)
    {
        if (players.contains(player))
        {
            Platform.runLater(() -> players.remove(player));
        }
    }


    public ObservableList<Player> getPlayers()
    {
        return players;
    }

//    public void setPlayers(final @NonNull ObservableList<Player> players)
//    {
//        this.players = players;
//    }

}
