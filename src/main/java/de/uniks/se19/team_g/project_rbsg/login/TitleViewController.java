package de.uniks.se19.team_g.project_rbsg.login;

import animatefx.animation.Bounce;
import io.rincl.*;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author Keanu Stückrad
 * @edited Georg Siebert
 */

@Controller
@Scope("prototype")
public class TitleViewController implements Rincled
{

    public Label title;
    public Label subtitle;

    public void init() {
        Bounce bounce = new Bounce(subtitle);
        bounce.setSpeed(1.5);
        bounce.setCycleCount(Animation.INDEFINITE);
        bounce.play();

        updateLabels();
    }

    private void updateLabels()
    {
        title.textProperty().setValue(getResources().getString("title"));
        subtitle.textProperty().setValue(getResources().getString("subtitle"));
    }
}
