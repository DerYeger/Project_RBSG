package de.uniks.se19.team_g.project_rbsg.Lobby.UI.CustomControls.Views;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
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

public class GameListViewCell extends ListCell<Game>
{
    @FXML
    public GridPane gridPane;
    @FXML
    public ImageView gameImageView;
    @FXML
    public Label nameLabel;
    @FXML
    public ImageView playersImageView;
    @FXML
    public Label playersLabel;
    @FXML
    public ImageView joinImageView;

    private FXMLLoader fxmlLoader;


    @Override
    protected void updateItem(final Game game, final boolean empty) {
        super.updateItem(game, empty);
        if(game == null || empty) {
            this.setText(null);
            this.setGraphic(null);
        }
        else {
            if(fxmlLoader == null) {
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("GameListCell.fxml"));
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

            nameLabel.setText(game.getName());

            playersLabel.setText(String.format("%s/%s", game.getJoinedPlayer(), game.getNeededPlayer()));

            Image gameImage = new Image(String.valueOf(getClass().getResource("Images/baseline_videogame_asset_white_48dp.png")));
            gameImageView.setImage(gameImage);

            Image playersImage = new Image(String.valueOf(getClass().getResource("Images/baseline_group_white_48dp.png")));
            playersImageView.setImage(playersImage);


            Image joinImage = new Image(String.valueOf(getClass().getResource("Images/baseline_last_page_white_48dp.png")));
            joinImageView.setImage(joinImage);

            this.setText(null);
            this.setGraphic(gridPane);
            this.setId("gameCell" + game.getName());

            joinImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseLeftClickOnJoin);

            // For testing
            joinImageView.setId("gameCellJoin" +  game.getName());
        }
    }

    private void mouseLeftClickOnJoin(final @NonNull MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            System.out.println("LeftClick on " + this.getId());
        }
    }
}
