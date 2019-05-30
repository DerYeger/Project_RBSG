package de.uniks.se19.team_g.project_rbsg.lobby.ui;

import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.springframework.lang.NonNull;

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
                this.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseRightCLick);
            }


            playerListCellLabel.setText(player.getName());
            Image image = new Image(String.valueOf(getClass().getResource("Images/" + player.getImagePath())));

            playerListCellImageView.setImage(image);

            this.setText(null);
            this.setGraphic(playerListCellGridPane);
            this.setId("playerCell"+player.getName());
        }

    }

    private void mouseRightCLick(final @NonNull MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            System.out.println("RightClick on " + this.getId());
            //TODO: Add behavior
        }
    }
}
