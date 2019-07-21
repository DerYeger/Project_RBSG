package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.net.URL;

/**
 * @author Jan Müller
 */
@Controller
@Scope("prototype")
public class ConfirmationAlertController extends AlertController {

    @FXML
    private Label label;
    @FXML
    private Button confirm;
    @FXML
    private Button cancel;

    private static final URL CONFIRM_WHITE = ConfirmationAlertController.class.getResource("/assets/icons/navigation/checkWhite.png");
    private static final URL CONFIRM_BLACK = ConfirmationAlertController.class.getResource("/assets/icons/navigation/checkBlack.png");
    private static final URL CANCEL_WHITE = ConfirmationAlertController.class.getResource("/assets/icons/navigation/crossWhite.png");
    private static final URL CANCEL_BLACK = ConfirmationAlertController.class.getResource("/assets/icons/navigation/crossBlack.png");

    private Runnable onConfirmRunnable;
    private Runnable onCancelRunnable;

    @Override
    protected void init() {
        label.textProperty().setValue(text);

        confirm.setOnAction(event -> {
            hide();
            if (onConfirmRunnable != null) {
                onConfirmRunnable.run();
            }
        });

        cancel.setOnAction(event ->{
            if (onCancelRunnable != null) {
                onCancelRunnable.run();
            }
            hide();
        });

        JavaFXUtils.setButtonIcons(confirm, CONFIRM_WHITE, CONFIRM_BLACK, 40);
        JavaFXUtils.setButtonIcons(cancel, CANCEL_WHITE, CANCEL_BLACK, 40);
    }

    public ConfirmationAlertController andThen(@NonNull final Runnable onConfirmRunnable) {
        this.onConfirmRunnable = onConfirmRunnable;
        return this;
    }

    public ConfirmationAlertController orElse(@Nullable final Runnable onCancelRunnable) {
        this.onCancelRunnable = onCancelRunnable;
        return this;
    }
}
