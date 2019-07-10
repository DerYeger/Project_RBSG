package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.waiting_room.PlayerCardBuilder;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

public class PlayerListController {

    private ObservableList<PlayerCardBuilder> playerCardBuilders;
    private ObservableList<Node> cards;
    private Game game;

    public PlayerListController(Game game){

        this.playerCardBuilders = FXCollections.observableArrayList();
        this.cards = FXCollections.observableArrayList();
        this.game=game;
        initPlayerCards();
        createCardNodes();

    }

    private void initPlayerCards(){
        for(int i=0; i<game.getPlayers().size();i++){
            playerCardBuilders.add(new PlayerCardBuilder());
        }
    }

    private void createCardNodes() {
        for(PlayerCardBuilder cardBuilder : playerCardBuilders){
            cards.add(cardBuilder.buildPlayerCard());
        }
    }

    public ObservableList<Node> getPlayerCards() {
        return cards;
    }
}
