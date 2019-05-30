package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.lobby.model.Game;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//    @FXML
//    public ImageView joinImageView;
    @FXML
    public Button joinButton;

    private FXMLLoader fxmlLoader;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Game game;

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


//            Image joinImage = new Image(String.valueOf(getClass().getResource("Images/baseline_last_page_white_48dp.png")));
//            joinImageView.setImage(joinImage);

            this.setText(null);
            this.setGraphic(gridPane);
            this.setId("gameCell" + game.getName());

            joinButton.setText(null);
            ImageView joinImageViewNonHover = new ImageView();
            ImageView joinImageViewHover = new ImageView();

            joinImageViewHover.setFitHeight(40);
            joinImageViewHover.setFitWidth(40);

            joinImageViewNonHover.setFitHeight(40);
            joinImageViewNonHover.setFitWidth(40);

            joinImageViewHover.setImage(new Image(String.valueOf(getClass().getResource("Images/baseline_last_page_black_48dp.png"))));
            joinImageViewNonHover.setImage(new Image(String.valueOf(getClass().getResource("Images/baseline_last_page_white_48dp.png"))));

            joinButton.graphicProperty().bind(Bindings.when(joinButton.hoverProperty())
                                                      .then(joinImageViewHover)
                                                      .otherwise(joinImageViewNonHover));

            joinButton.setOnAction(this::joinButtonClicked);

//            joinImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseLeftClickOnJoin);

            // For testing
//            joinImageView.setId("gameCellJoin" +  game.getName());
            joinButton.setId("joinGameButton"+ game.getName());

            this.game = game;
        }
    }

    private void joinButtonClicked(ActionEvent event)
    {
        logger.debug("You want to join " + game.getName());
    }

//    private void mouseLeftClickOnJoin(final @NonNull MouseEvent event) {
//        if(event.getButton() == MouseButton.PRIMARY) {
//            System.out.println("LeftClick on " + this.getId());
//        }
//    }
}