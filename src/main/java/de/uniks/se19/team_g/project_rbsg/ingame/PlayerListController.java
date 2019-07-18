package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.PlayerCardBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class PlayerListController {

    private ObservableList<PlayerCardBuilder> playerCardBuilders;
    private ObservableList<Node> cards;
    private Game game;


    public PlayerListController(Game game){

        this.playerCardBuilders = FXCollections.observableArrayList();
        this.cards = FXCollections.observableArrayList();
        this.game=game;
        initPlayerCards(game.getPlayers());
    }

    private void initPlayerCards(ObservableList<Player> players){
        for(int i=0; i<game.getPlayers().size();i++){
            PlayerCardController playerCardController = new PlayerCardController();
            cards.add(playerCardController.createPlayerCard(players.get(i)));
        }
    }

    public Node createLoserCard(Player player){
        PlayerCardController playerCardController = new PlayerCardController();
        return playerCardController.createLoserCard(player);
    }

    public ObservableList<Node> getPlayerCards() {

        return cards;
    }
}
