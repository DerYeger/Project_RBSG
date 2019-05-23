package de.uniks.se19.team_g.project_rbsg.Lobby.UI.CustomControls.Views;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Georg Siebert
 */

@Component
public class PlayerListViewCell extends ListCell<Player>
{
    @FXML
    private Label playerListCellLabel;

    @FXML
    private ImageView playerListCellImageView;

    @FXML
    private GridPane playerListCellGridPane;

    private FXMLLoader fxmlLoader;

    private boolean alreadyCreated = false;

    @Override
    protected void updateItem(final Player player, final boolean empty)
    {
        super.updateItem(player, empty);

        if (empty || player == null)
        {
            this.setText(null);
            this.setGraphic(null);
        }
        else
        {
            if (fxmlLoader == null)
            {
                fxmlLoader = new FXMLLoader();
            }

            if(!alreadyCreated) {
                fxmlLoader.setLocation(getClass().getResource("PlayerListCell.fxml"));
                fxmlLoader.setController(this);
                try
                {
                    fxmlLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                alreadyCreated = true;
            }

            playerListCellLabel.setText(player.getName());
            Image image = new Image(String.valueOf(getClass().getResource("Images/" + player.getImagePath())));

            playerListCellImageView.setImage(image);

            this.setText(null);
            this.setGraphic(playerListCellGridPane);
            this.setId("playerCell"+player.getName());
        }
    }
}
