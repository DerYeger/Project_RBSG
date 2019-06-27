package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;

public abstract class ConfirmationAlertController extends AlertController {

    @FXML
    private Label label;
    @FXML
    private Button confirm;
    @FXML
    private Button cancel;

    private URL acceptBlack = getClass().getResource("/assets/icons/navigation/checkBlack.png");
    private URL acceptWhite = getClass().getResource("/assets/icons/navigation/checkWhite.png");
    private URL cancelBlack = getClass().getResource("/assets/icons/navigation/crossBlack.png");
    private URL cancelWhite = getClass().getResource("/assets/icons/navigation/crossWhite.png");

    public void initialize() {
        label.textProperty().setValue(getText());
        confirm.setOnAction(event -> onConfirm());
        cancel.setOnAction(event -> hide());

        JavaFXUtils.setButtonIcons(confirm, acceptWhite, acceptBlack, 40);
        JavaFXUtils.setButtonIcons(cancel, cancelWhite, cancelBlack, 40);
    }

    protected abstract String getText();
    protected abstract void onConfirm();
}
