package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.IOException;

public class PlayerCardController {
    private FXMLLoader loader;
    public Label playerNameLabel;
    public ImageView playerImage;
    public Pane colorPane;


    public PlayerCardController(){
        loader=new FXMLLoader(getClass().getResource("/ui/ingame/playerCard.fxml"));
        loader.setController(this);
    }

    public Node createPlayerCard(Player player){
        Node playerCard=null;
        try {
            playerCard=loader.load();
            playerNameLabel.setText(player.getName());
            playerImage.setImage(new Image((getClass().getResource("/assets/icons/operation/accountWhite.png")).toString()));
            colorPane.setBackground(new Background(new BackgroundFill(Color.valueOf(player.getColor()), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerCard;
    }

}
