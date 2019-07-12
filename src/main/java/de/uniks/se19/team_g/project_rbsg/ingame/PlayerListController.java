package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.waiting_room.PlayerCardBuilder;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
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
        //createCardNodes();
    }

    private void initPlayerCards(ObservableList<Player> players){
        for(int i=0; i<game.getPlayers().size();i++){
            /*PlayerCardBuilder card = new PlayerCardBuilder();
            playerCardBuilders.add(card);*/
            PlayerCardController playerCardController = new PlayerCardController();
            cards.add(playerCardController.createPlayerCard(players.get(0)));
        }
    }

    private void createCardNodes() {
        for(PlayerCardBuilder cardBuilder : playerCardBuilders){
            Node node = cardBuilder.buildPlayerCard();
            cardBuilder.setPlayer(game.getPlayers().get(0), Color.valueOf(game.getPlayers().get(0).getColor()));
            node.scaleXProperty().setValue(0.75);
            node.scaleYProperty().setValue(0.75);
            cards.add(node);
        }
    }

    public ObservableList<Node> getPlayerCards() {
        return cards;
    }
}
