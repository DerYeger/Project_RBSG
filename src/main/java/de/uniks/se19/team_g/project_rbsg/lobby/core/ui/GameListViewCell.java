package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincl;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Georg Siebert
 * @edited Keanu Stückrad
 */
@Component
@Scope("prototype")
public class GameListViewCell extends ListCell<Game> implements Initializable
{
    public GridPane gridPane;
    public ImageView gameImageView;
    public Label nameLabel;
    public ImageView playersImageView;
    public Label playersLabel;
    public Button joinButton;
    public Pane joinButtonContainer;
    public Button spectatorButton;

    private FXMLLoader fxmlLoader;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Nullable
    private Game game;

    @Nonnull
    private final GameProvider gameProvider;
    @Nonnull
    private final UserProvider userProvider;
    @Nonnull
    private final SceneManager sceneManager;
    @Nonnull
    private final JoinGameManager joinGameManager;
    @Nonnull
    private final ApplicationState appState;

    public GameListViewCell(
            @Nonnull final GameProvider gameProvider,
            @Nonnull final UserProvider userProvider,
            @Nonnull final SceneManager sceneManager,
            @Nonnull final JoinGameManager joinGameManager,
            @Nonnull final ApplicationState appState
        ){
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
        this.joinGameManager = joinGameManager;
        this.appState = appState;
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
            joinButton.disableProperty()
                    .bind(Bindings
                                  .when(game.getJoinedPlayerProperty().isEqualTo(game.getNeededPlayer()))
                                  .then(true)
                                  .otherwise(false));


            spectatorButton.setOnAction(this::joinSpectating);
            spectatorButton.setId("spectatorButton" + game.getName());

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
        Objects.requireNonNull(game);

        Platform.runLater(() -> {
            playersLabel.setText(String.format("%d/%d", game.getJoinedPlayer(), game.getNeededPlayer()));
        });

        logger.debug("Updated joined Player to" + game.getJoinedPlayer());
    }

    private void joinButtonClicked(ActionEvent event)
    {
        if(game != null) {
            try {
                gameProvider.set(game);
                joinGameManager.joinGame(userProvider.get(), game).get();
                sceneManager.setScene(SceneManager.SceneIdentifier.INGAME, false, null);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                gameProvider.clear();
            }
        }
    }

    public void joinSpectating(ActionEvent event){
        if (this.game != null) {
            this.game.setSpectatorModus(true);
        }
        if(game != null) {
            gameProvider.set(game);
                //joinGameManager.joinGame(userProvider.get(), game).get();
            sceneManager.setScene(SceneManager.SceneIdentifier.INGAME, false, null);
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

        JavaFXUtils.bindButtonDisableWithTooltip(
            joinButton,
            joinButtonContainer,
            new SimpleStringProperty(Rincl.getResources(ProjectRbsgFXApplication.class).getString("ValidArmyRequired")),
            appState.hasPlayableArmies
        );

        JavaFXUtils.setButtonIcons(
                spectatorButton,
                getClass().getResource("/assets/icons/operation/spectatorWhite.png"),
                getClass().getResource("/assets/icons/operation/spectatorBlack.png"),
                40
        );


    }
}
