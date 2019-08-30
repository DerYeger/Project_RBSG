package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.Locale;

public class PhaseLabelController {

    public Label playerLabel;
    public Label roundLabel;
    public Label numberLabel;
    public ImageView phaseImageView;
    public ImageView playerImageView;
    private FXMLLoader fxmlLoader;
    private Node phaseLabelView;

    public Node buildPhaseLabel(
            Property<Locale> selectedLocale,
            ObjectProperty<Image> phaseImageProperty,
            ObjectProperty<Player> playerProperty,
            SimpleIntegerProperty roundCountProperty
    ) {
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/ingame/phaseLabel.fxml"));
            fxmlLoader.setController(this);
            try {
                phaseLabelView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        playerImageView.setImage(new Image("/assets/icons/navigation/accountWhite.png"));

        roundLabel.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "roundLabel"));
        phaseImageView.imageProperty().bind(phaseImageProperty);
        SimpleStringProperty playerNameProperty = new SimpleStringProperty("");
        playerProperty.addListener(((observable, oldValue, newValue) -> playerNameProperty.set(newValue.getName())));
        playerLabel.textProperty().bind(playerNameProperty);
        numberLabel.textProperty().bind(roundCountProperty.asString());

        return phaseLabelView;
    }

}
