package de.uniks.se19.team_g.project_rbsg.Login_Registration;

import animatefx.animation.Bounce;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

/**
 * @author Keanu Stückrad
 */

@Controller
public class TitleFormController {

    @FXML
    Label subtitle;

    public void init() {
        Bounce bounce = new Bounce(subtitle);
        bounce.setCycleCount(Animation.INDEFINITE);
        bounce.play();
    }
}
