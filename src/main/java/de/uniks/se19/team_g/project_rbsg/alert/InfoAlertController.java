package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.net.URL;

/**
 * @author Jan Müller
 */
@Controller
@Scope("prototype")
public class InfoAlertController extends AlertController {

    private static final URL CONFIRM_WHITE = InfoAlertController.class.getResource("/assets/icons/navigation/checkWhite.png");
    private static final URL CONFIRM_BLACK = InfoAlertController.class.getResource("/assets/icons/navigation/checkBlack.png");

    @FXML
    private Label label;
    @FXML
    private Button confirm;
    @FXML
    private VBox container;

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

        container.setOnKeyReleased(event -> {
            /*if (onConfirmRunnable != null) {
                onConfirmRunnable.run();
            } else {
                hide();
            }*/
            System.out.println("Info alert");
        });

        JavaFXUtils.setButtonIcons(confirm, CONFIRM_WHITE, CONFIRM_BLACK, 40);
    }

    public InfoAlertController andThen(@Nullable final Runnable onConfirmRunnable) {
        this.onConfirmRunnable = onConfirmRunnable;
        return this;
    }
}
