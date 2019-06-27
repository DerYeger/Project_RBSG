package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;

@Controller
@Scope("prototype")
public class ExitAlertController extends AlertController {

    //PLACEHOLDER TODO replace with localized text
    private static final String text = "Are you sure you want to leave the game?";

    private URL acceptBlack = getClass().getResource("/assets/icons/navigation/checkBlack.png");
    private URL acceptWhite = getClass().getResource("/assets/icons/navigation/checkWhite.png");
    private URL cancelBlack = getClass().getResource("/assets/icons/navigation/crossBlack.png");
    private URL cancelWhite = getClass().getResource("/assets/icons/navigation/crossWhite.png");

    @FXML
    private Label label;
    @FXML
    private Button accept;
    @FXML
    private Button cancel;

    public void initialize() {
        label.textProperty().setValue(text);
        accept.setOnAction(event -> Platform.exit());
        cancel.setOnAction(event -> hide());

        JavaFXUtils.setButtonIcons(accept, acceptWhite, acceptBlack, 40);
        JavaFXUtils.setButtonIcons(cancel, cancelWhite, cancelBlack, 40);
    }
}
