package de.uniks.se19.team_g.project_rbsg.Ingame;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class PlayerCard {

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
            fxmlLoader = new FXMLLoader(getClass().getResource("/de/uniks/se19/team_g/project_rbsg/Lobby/UI/CustomControls/Views/PlayerListCell.fxml"));
            fxmlLoader.setController(this);
            try {
                playerCardView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerListCellLabel.setText("<missing>");
        Image image = new Image(String.valueOf(getClass().getResource("Images/baseline_help_black_48dp.png")));
        playerListCellImageView.setImage(image);
        return playerCardView;
    }

    public Node setPlayer(Player player){
        if(fxmlLoader == null) {
            buildPlayerCard();
        }
        playerListCellLabel.setText(player.getName());
        Image image = new Image(String.valueOf(getClass().getResource("/de/uniks/se19/team_g/project_rbsg/Lobby/UI/CustomControls/Views/Images/" + player.getImagePath())));
        playerListCellImageView.setImage(image);
        return playerCardView;
    }

}
