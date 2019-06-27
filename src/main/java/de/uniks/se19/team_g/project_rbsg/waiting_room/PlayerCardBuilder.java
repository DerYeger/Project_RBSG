package de.uniks.se19.team_g.project_rbsg.waiting_room;

import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
public class PlayerCardBuilder {

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

    private FXMLLoader fxmlLoader;
    private Node playerCardView;
    private SimpleDoubleProperty progress;

    public Node buildPlayerCard(){
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

        Image image = new Image(String.valueOf(getClass().getResource("/assets/icons/navigation/accountWhite.png")));
        playerListCellImageView.setImage(image);

        setEmpty();

        return playerCardView;
    }

    private void setEmpty() {
        Platform.runLater(()-> playerListCellLabel.setText("Waiting for\nplayer..."));
        Platform.runLater(()-> progressIndicator.setVisible(true));
        Platform.runLater(()-> playerListCellImageView.setVisible(false));
        playerListCellLabel.getStyleClass().remove("player");
        playerListCellLabel.getStyleClass().add("waiting");
    }

    public Node playerLeft() {
        if(fxmlLoader == null) {
            buildPlayerCard();
        } else {
            setEmpty();
            deleteColor();
        }
        return playerCardView;
    }

    public Node setPlayer(Player player, Color color){
        if(fxmlLoader == null) {
            buildPlayerCard();
        }
        Platform.runLater(()-> playerListCellLabel.setText(player.getName()));
        setReady();
        setColor(color);
        return playerCardView;
    }

    private void setReady() {
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

}
