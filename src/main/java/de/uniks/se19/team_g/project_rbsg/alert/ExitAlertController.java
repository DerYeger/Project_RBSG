package de.uniks.se19.team_g.project_rbsg.alert;

import javafx.application.Platform;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author Jan Müller
 */
@Controller
@Scope("prototype")
public class ExitAlertController extends ConfirmationAlertController {

    //TODO replace placeholder
    @Override
    protected String getText() {
        return "Are you sure you want to leave the game?";
    }

    @Override
    protected void onConfirm() {
        Platform.exit();
    }
}
