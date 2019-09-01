package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import de.uniks.se19.team_g.project_rbsg.bots.Bot;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Locale;

/**
 * @author  Keanu Stückrad
 */
public class PlayerCardBuilder {

    public static final String READY_STYLE = "ready";
    public Label playerListCellLabel;
    public ImageView playerListCellImageView;
    public ProgressIndicator progressIndicator;
    public Pane colorPane;
    public Button botButton;
    public Pane botButtonContainer;
    public Pane root;

    private FXMLLoader fxmlLoader;
    private Node playerCardView;
    private SimpleDoubleProperty progress;

    private Player player;
    private ChangeListener<Boolean> onPlayerChangedReadyState;
    private Image whiteAccountImage;
    private Image blackAccountImage;


    public final BooleanProperty emptyProperty = new SimpleBooleanProperty(true);
    private final ObjectProperty<EventHandler<ActionEvent>> interaction = new SimpleObjectProperty<>();

    private Property<Locale> selectedLocale;
    private Runnable onBotRequested;

    public PlayerCardBuilder() {
    }

    public void setOnBotRequested(Runnable onBotRequested) {
        this.onBotRequested = onBotRequested;
        if (botButton != null) {
            botButton.setDisable(false);
        }
    }

    public void setOnPlayerClicked(EventHandler<MouseEvent> handler) {
        // TODO: needs to be a pane behind the button. bot buttons require priority
        // root.setOnMouseClicked(handler);
    }


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


        BooleanBinding notEmptyBinding = emptyProperty.not();
        progressIndicator.visibleProperty().bind(emptyProperty);
        playerListCellImageView.visibleProperty().bind(notEmptyBinding);
        colorPane.visibleProperty().bind(notEmptyBinding);
        colorPane.managedProperty().bind(notEmptyBinding);

        final BooleanBinding hasInteraction = interaction.isNotNull();
        botButton.onActionProperty().bind(interaction);
        botButtonContainer.visibleProperty().bind(hasInteraction);
        botButtonContainer.managedProperty().bind(hasInteraction);

        setEmpty();

        return playerCardView;
    }

    private void handleBotRequest(ActionEvent actionEvent) {
        if (onBotRequested == null) {
            return;
        }
        onBotRequested.run();
    }

    private void setEmpty() {
        emptyProperty.setValue(true);

        final ObservableList<String> styles = playerListCellLabel.getStyleClass();
        playerCardView.getStyleClass().remove(READY_STYLE);
        styles.remove("columnPlayer");
        styles.add("waiting");
        if(selectedLocale != null) playerListCellLabel.textProperty().bind(
                        Bindings.createStringBinding(() -> Rincl.getResources(PlayerCardBuilder.class).getString("waiting"),
                        selectedLocale
                )
        );
        onPlayerChangedReadyState = null;

        interaction.set(actionEvent -> {
            handleBotRequest(actionEvent);
            actionEvent.consume();
        });
        if (onBotRequested == null) {
            botButton.setDisable(true);
        }

        JavaFXUtils.setButtonIcons(
                botButton,
                getClass().getResource("/assets/icons/operation/botWhite.png"),
                getClass().getResource("/assets/icons/operation/botBlack.png"),
                40
        );

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
        interaction.set(null);
        this.player = player;
        if(fxmlLoader == null) {
            buildPlayerCard(selectedLocale);
        }
        playerListCellLabel.textProperty().unbind();
        playerListCellLabel.setText(player.getName());
        setReady();
        setColor(color);

        // doing it like this saves us from the trouble of removing the old listener, if the old columnPlayer would be updated
        // also, when we switch to ingame, the listener is removed as well.
        onPlayerChangedReadyState = this::onPlayerChangedReadyState;
        player.isReadyProperty().addListener(new WeakChangeListener<>(onPlayerChangedReadyState));
        onPlayerChangedReadyState(player.isReadyProperty(), player.getIsReady(), player.getIsReady());

        return playerCardView;

    }

    private void setReady() {
        emptyProperty.setValue(false);

        playerListCellLabel.getStyleClass().remove("waiting");
        playerListCellLabel.getStyleClass().add("columnPlayer");
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

    public boolean isEmpty() {
        return emptyProperty.get();
    }

    public void setNodeOrientation(NodeOrientation orientation) {
        root.setNodeOrientation(orientation);
    }

    private void handle(ActionEvent event) {
        onBotRequested.run();
    }

    public void configureKillButton(Bot bot){
        interaction.set(event -> {
            bot.shutdown();
            event.consume();
        });
        JavaFXUtils.setButtonIcons(
                botButton,
                getClass().getResource("/assets/icons/operation/killBotWhite.png"),
                getClass().getResource("/assets/icons/operation/killBotBlack.png"),
                40
        );
    }
}
