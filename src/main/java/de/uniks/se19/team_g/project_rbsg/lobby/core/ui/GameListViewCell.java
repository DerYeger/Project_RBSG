package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Georg Siebert
 * @edited Keanu Stückrad
 */

public class GameListViewCell extends ListCell<Game> implements Initializable
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

    public GameListViewCell(
            @Nonnull final GameProvider gameProvider,
            @Nonnull final UserProvider userProvider,
            @Nonnull final SceneManager sceneManager,
            @Nonnull final JoinGameManager joinGameManager
    ){
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
            this.setText(null);
            this.setGraphic(getGraphicNode());
            this.setId("gameCell" + game.getName());

            nameLabel.setText(game.getName());

            game.getJoinedPlayerProperty().addListener(this::updateItem);
            playersLabel.setText(String.format("%d/%d", game.getJoinedPlayer(), game.getNeededPlayer()));
            playersLabel.setId("gameCellPlayersLabel" + game.getName());

            Image gameImage = new Image(String.valueOf(getClass().getResource("/assets/icons/navigation/videogameWhite.png")));
            gameImageView.setImage(gameImage);

            Image playersImage = new Image(String.valueOf(getClass().getResource("/assets/icons/navigation/groupWhite.png")));
            playersImageView.setImage(playersImage);


            joinButton.setOnAction(this::joinButtonClicked);

            joinButton.setId("joinGameButton"+ game.getName());

            this.game = game;
        }
    }

    private GridPane getGraphicNode() {
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ui/lobby/core/gameListCell.fxml"));
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

        return gridPane;
    }

    private void updateItem(Observable observable)
    {
        Platform.runLater(() -> {
            playersLabel.setText(String.format("%d/%d", game.getJoinedPlayer(), game.getNeededPlayer()));
        });

        logger.debug("Updated joined Player to" + game.getJoinedPlayer());
    }

    private void joinButtonClicked(ActionEvent event)
    {
        if(game != null) {
            gameProvider.set(game);
            joinGameManager.joinGame(userProvider.get(), game);
            sceneManager.setWaitingRoomScene();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        joinButton.setText(null);
        ImageView joinImageViewNonHover = new ImageView();
        ImageView joinImageViewHover = new ImageView();

        joinImageViewHover.setFitHeight(40);
        joinImageViewHover.setFitWidth(40);

        joinImageViewNonHover.setFitHeight(40);
        joinImageViewNonHover.setFitWidth(40);

        joinImageViewHover.setImage(new Image(String.valueOf(getClass().getResource("/assets/icons/navigation/lastPageBlack.png"))));
        joinImageViewNonHover.setImage(new Image(String.valueOf(getClass().getResource("/assets/icons/navigation/lastPageWhite.png"))));

        joinButton.graphicProperty().bind(Bindings.when(joinButton.hoverProperty())
                .then(joinImageViewHover)
                .otherwise(joinImageViewNonHover));


    }
}
