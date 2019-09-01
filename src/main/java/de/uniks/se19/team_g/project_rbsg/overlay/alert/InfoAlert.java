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
public class InfoAlert extends Overlay {

    private static final URL CONFIRM_WHITE = InfoAlert.class.getResource("/assets/icons/navigation/checkWhite.png");
    private static final URL CONFIRM_BLACK = InfoAlert.class.getResource("/assets/icons/navigation/checkBlack.png");

    @FXML
    private Label label;
    @FXML
    private Button confirm;

    private Runnable onConfirmRunnable;

    @Override
    protected void init() {
        label.textProperty().setValue(text);

        confirm.setOnAction(event -> {
            if (onConfirmRunnable != null) {
                onConfirmRunnable.run();
            } else {
                hide();
            }
        });

        JavaFXUtils.setButtonIcons(confirm, CONFIRM_WHITE, CONFIRM_BLACK, 40);
    }

    public InfoAlert andThen(@Nullable final Runnable onConfirmRunnable) {
        this.onConfirmRunnable = onConfirmRunnable;
        return this;
    }
}
