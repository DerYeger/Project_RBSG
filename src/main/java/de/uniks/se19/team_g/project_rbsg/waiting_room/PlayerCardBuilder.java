package de.uniks.se19.team_g.project_rbsg.waiting_room;

import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
public class PlayerCardBuilder {

    @FXML
    private Label playerListCellLabel;

    @FXML
    private ImageView playerListCellImageView;

    @FXML
    private GridPane playerListCellGridPane;

    private FXMLLoader fxmlLoader;
    private Node playerCardView;

    public Node buildPlayerCard(){
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/waiting_room/player-card.fxml"));
            fxmlLoader.setController(this);
            try {
                playerCardView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerListCellLabel.setText("<missing>");
        Image image = new Image(String.valueOf(getClass().getResource("/assets/icons/navigation/account-white.png")));
        playerListCellImageView.setImage(image);
        return playerCardView;
    }

    public Node setPlayer(Player player){
        if(fxmlLoader == null) {
            buildPlayerCard();
        }
        playerListCellLabel.setText(player.getName());
        Image image = new Image(String.valueOf(getClass().getResource(player.getImagePath())));
        playerListCellImageView.setImage(image);
        return playerCardView;
    }

}
