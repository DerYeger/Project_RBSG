package de.uniks.se19.team_g.project_rbsg.overlay.alert;

import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.net.URL;

/**
 * @author Jan Müller
 */
@Controller
@Scope("prototype")
public class ConfirmationAlert extends Overlay {

    @FXML
    private Label label;
    @FXML
    private Button confirm;
    @FXML
    private Button cancel;

    private static final URL CONFIRM_WHITE = ConfirmationAlert.class.getResource("/assets/icons/navigation/checkWhite.png");
    private static final URL CONFIRM_BLACK = ConfirmationAlert.class.getResource("/assets/icons/navigation/checkBlack.png");
    private static final URL CANCEL_WHITE = ConfirmationAlert.class.getResource("/assets/icons/navigation/crossWhite.png");
    private static final URL CANCEL_BLACK = ConfirmationAlert.class.getResource("/assets/icons/navigation/crossBlack.png");

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

        cancel.setOnAction(event -> {
            hide();
            if (onCancelRunnable != null) {
                onCancelRunnable.run();
            }
        });

        JavaFXUtils.setButtonIcons(confirm, CONFIRM_WHITE, CONFIRM_BLACK, 40);
        JavaFXUtils.setButtonIcons(cancel, CANCEL_WHITE, CANCEL_BLACK, 40);
    }

    public ConfirmationAlert andThen(@Nullable final Runnable onConfirmRunnable) {
        this.onConfirmRunnable = onConfirmRunnable;
        return this;
    }

    public ConfirmationAlert orElse(@Nullable final Runnable onCancelRunnable) {
        this.onCancelRunnable = onCancelRunnable;
        return this;
    }
}