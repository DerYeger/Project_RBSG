package de.uniks.se19.team_g.project_rbsg.lobby.CrossCutting.DataClasses;

import de.uniks.se19.team_g.project_rbsg.lobby.Logic.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;

/**
 * @author Georg Siebert
 */

public class Lobby
{
    private SystemMessageManager systemMessageManager;
    private ObservableList<Game> games;
    private ObservableList<Player> players;
    private ChatController chatController;

    public Lobby()
    {
        games = FXCollections.observableArrayList();
        players = FXCollections.observableArrayList();
    }

    public ChatController getChatController() {
        return chatController;
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
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

    public void clearGames() {
        Platform.runLater(() -> games.clear());
    }

    public void clearPlayers() {
        Platform.runLater(() -> players.clear());
    }

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


    public void removePlayerByName(final @NonNull String name) {
        int counter = 0;
        for(Player player : players) {
            if(player.getName().equals(name)) {
                int finalCounter = counter;
                Platform.runLater(()-> players.remove(finalCounter));
            }
            else {
                counter++;
            }
        }
    }

    public final @Nullable Game getGameOverId(final @NonNull String id) {
        for (Game game : games)
        {
            if(game.getId().equals(id)) {
                return game;
            }
        }
        return null;
    }

    public final @Nullable Player getPlayerByName(final @NonNull String name) {
        for(Player player : players) {
            if(player.getName().equals(name)) {
                return player;
            }
        }
        return null;
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
