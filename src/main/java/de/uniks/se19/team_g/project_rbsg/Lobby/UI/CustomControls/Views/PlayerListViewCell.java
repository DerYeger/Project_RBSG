package de.uniks.se19.team_g.project_rbsg.Lobby.UI.CustomControls.Views;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * @author Georg Siebert
 */

public class PlayerListViewCell extends ListCell<Player>
{
    @FXML
    private Label playerListCellLabel;

    @FXML
    private ImageView playerListCellImageView;

    @FXML
    private GridPane playerListCellGridPane;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(final Player player,final boolean empty)
    {
        super.updateItem(player, empty);

        if (empty || player == null)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            if(fxmlLoader == null)
            {
                fxmlLoader = new FXMLLoader(getClass().getResource("PlayerListCell.fxml"));
                fxmlLoader.setController(this);
                try
                {
                    fxmlLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            playerListCellLabel.setText(player.getName());
            Image image = new Image(String.valueOf(getClass().getResource("Images/" + player.getImagePath())));

            playerListCellImageView.setImage(image);

            setText(null);
            setGraphic(playerListCellGridPane);
        }
    }
}
