package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import javafx.application.*;
import javafx.beans.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import java.io.IOException;

/**
 * @author Georg Siebert
 * @edited Keanu Stückrad
 */

public class GameListViewCell extends ListCell<Game>
{
    public GridPane gridPane;
    public ImageView gameImageView;
    public Label nameLabel;
    public ImageView playersImageView;
    public Label playersLabel;
    public Button joinButton;

    private FXMLLoader fxmlLoader;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Game game;

    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    private final JoinGameManager joinGameManager;

    @Autowired
    public GameListViewCell(@NonNull final GameProvider gameProvider, @NonNull final UserProvider userProvider, @NonNull final SceneManager sceneManager, @NonNull final JoinGameManager joinGameManager){
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
        this.joinGameManager = joinGameManager;
    }

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

            game.getJoinedPlayerProperty().addListener(this::updateItem);
            playersLabel.setText(String.format("%s/%s", game.getJoinedPlayer(), game.getNeededPlayer()));
            playersLabel.setId("gameCellPlayersLabel" + game.getName());

            Image gameImage = new Image(String.valueOf(getClass().getResource("Images/baseline_videogame_asset_white_48dp.png")));
            gameImageView.setImage(gameImage);

            Image playersImage = new Image(String.valueOf(getClass().getResource("Images/baseline_group_white_48dp.png")));
            playersImageView.setImage(playersImage);



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

            joinButton.setId("joinGameButton"+ game.getName());

            this.game = game;
        }
    }

    private void updateItem(Observable observable)
    {
        Platform.runLater(() -> {
            playersLabel.setText(String.format("%s/%s", game.getJoinedPlayer(), game.getNeededPlayer()));
        });

        logger.debug("Updated joined Player to" + game.getJoinedPlayer());
    }

    private void joinButtonClicked(ActionEvent event)
    {
        if(game != null) {
            gameProvider.set(game);
            joinGameManager.joinGame(userProvider.get(), game);
            sceneManager.setIngameScene();
        }
    }

}
