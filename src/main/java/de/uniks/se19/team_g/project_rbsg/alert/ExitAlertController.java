package de.uniks.se19.team_g.project_rbsg.alert;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class ExitAlertController extends AlertController {

    //PLACEHOLDER TODO replace with localized text
    private static final String text = "Are you sure you want to leave the game?";

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
    }
}
