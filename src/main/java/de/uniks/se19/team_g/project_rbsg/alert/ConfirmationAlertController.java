package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.lang.NonNull;

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

    private Runnable onConfirmRunnable;
    private Runnable onCancelRunnable;

    public void initialize() {
        label.textProperty().setValue(getText());

        confirm.setOnAction(event -> {
            if (onConfirmRunnable != null) {
                onConfirmRunnable.run();
            }
        });

        cancel.setOnAction(event ->{
            if (onCancelRunnable != null) {
                onCancelRunnable.run();
            } else {
                hide();
            }
        });

        JavaFXUtils.setButtonIcons(confirm, acceptWhite, acceptBlack, 40);
        JavaFXUtils.setButtonIcons(cancel, cancelWhite, cancelBlack, 40);
    }

    protected abstract String getText();

    public ConfirmationAlertController andThen(@NonNull final Runnable onConfirmRunnable) {
        this.onConfirmRunnable = onConfirmRunnable;
        return this;
    }

    public ConfirmationAlertController orElse(@NonNull final Runnable onCancelRunnable) {
        this.onCancelRunnable = onCancelRunnable;
        return this;
    }
}
