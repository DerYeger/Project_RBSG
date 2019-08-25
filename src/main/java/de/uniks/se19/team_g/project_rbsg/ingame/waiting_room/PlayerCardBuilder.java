package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import io.rincl.Rincl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Locale;

/**
 * @author  Keanu Stückrad
 */
public class PlayerCardBuilder {

    public static final String READY_STYLE = "ready";
    public Label playerListCellLabel;
    public ImageView playerListCellImageView;
    public StackPane playerStackPane;
    public ProgressIndicator progressIndicator;
    public ColumnConstraints column0;
    public ColumnConstraints column1;
    public ColumnConstraints column00;
    public ColumnConstraints column11;
    public GridPane playerLabelColorPane;
    public Pane colorPane;
    public GridPane playerCardGridPane;

    private FXMLLoader fxmlLoader;
    private Node playerCardView;
    private SimpleDoubleProperty progress;

    public boolean isEmpty;
    private Player player;
    private ChangeListener<Boolean> onPlayerChangedReadyState;
    private Image whiteAccountImage;
    private Image blackAccountImage;

    private Property<Locale> selectedLocale;

    public Node buildPlayerCard(Property<Locale> selectedLocale){
        this.selectedLocale = selectedLocale;
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/waiting_room/playerCard.fxml"));
            fxmlLoader.setController(this);
            try {
                playerCardView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(progress == null){
            progress = new SimpleDoubleProperty(0.0);
        }
        progressIndicator.progressProperty().bind(progress);
        Timeline progressTimeline = setupTimeline();
        progressTimeline.setCycleCount(Animation.INDEFINITE);
        progressTimeline.play();

        whiteAccountImage = new Image(getClass().getResource("/assets/icons/navigation/accountWhite.png").toExternalForm());
        blackAccountImage = new Image(getClass().getResource("/assets/icons/navigation/accountBlack.png").toExternalForm());

        setEmpty();

        return playerCardView;
    }

    private void setEmpty() {
        isEmpty = true;
        final ObservableList<String> styles = playerListCellLabel.getStyleClass();
        playerCardView.getStyleClass().remove(READY_STYLE);
        styles.remove("player");
        styles.add("waiting");
        if(selectedLocale != null) playerListCellLabel.textProperty().bind(
                        Bindings.createStringBinding(() -> Rincl.getResources(PlayerCardBuilder.class).getString("waiting"),
                        selectedLocale
                )
        );
        progressIndicator.setVisible(true);
        playerListCellImageView.setVisible(false);
        onPlayerChangedReadyState = null;
    }

    public Node playerLeft() {
        if(fxmlLoader == null) {
            buildPlayerCard(selectedLocale);
        } else {
            setEmpty();
            deleteColor();
        }
        return playerCardView;
    }

    public Node setPlayer(Player player, Color color){

        this.player = player;
        if(fxmlLoader == null) {
            buildPlayerCard(selectedLocale);
        }
        playerListCellLabel.textProperty().unbind();
        Platform.runLater(()-> playerListCellLabel.setText(player.getName()));
        setReady();
        setColor(color);

        // doing it like this saves us from the trouble of removing the old listener, if the old player would be updated
        // also, when we switch to ingame, the listener is removed as well.
        onPlayerChangedReadyState = this::onPlayerChangedReadyState;
        player.isReadyProperty().addListener(new WeakChangeListener<>(onPlayerChangedReadyState));
        onPlayerChangedReadyState(player.isReadyProperty(), player.getIsReady(), player.getIsReady());

        return playerCardView;

    }

    private void setReady() {
        isEmpty = false;
        Platform.runLater(()-> progressIndicator.setVisible(false));
        Platform.runLater(()-> playerListCellImageView.setVisible(true));
        playerListCellLabel.getStyleClass().remove("waiting");
        playerListCellLabel.getStyleClass().add("player");
    }

    private Timeline setupTimeline() {
        return new Timeline(
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(progress, 1.0)
                )
        );
    }

    private void setColor(Color color){
        Platform.runLater(()-> colorPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))));
    }

    private void deleteColor() {
        Platform.runLater(()-> colorPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public void switchColumns() {
        GridPane.setColumnIndex(playerStackPane, 1);
        GridPane.setColumnIndex(playerLabelColorPane, 0);
        GridPane.setColumnIndex(playerListCellLabel, 1);
        GridPane.setColumnIndex(colorPane, 0);
        GridPane.setHalignment(playerListCellLabel, HPos.RIGHT);
        column0.setPrefWidth(250);
        column0.setMinWidth(250);
        column0.setMaxWidth(250);
        column1.setPrefWidth(100);
        column1.setMinWidth(100);
        column1.setMaxWidth(100);
        column00.setPrefWidth(210);
        column00.setMinWidth(210);
        column00.setMaxWidth(210);
        column11.setPrefWidth(40);
        column11.setMinWidth(40);
        column11.setMaxWidth(40);
    }

    public Player getPlayer() {
        return this.player;
    }

    private void onPlayerChangedReadyState(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            playerCardView.getStyleClass().add(READY_STYLE);
            playerListCellImageView.setImage(blackAccountImage);
        } else {
            playerCardView.getStyleClass().remove(READY_STYLE);
            playerListCellImageView.setImage(whiteAccountImage);
        }
    }
}
