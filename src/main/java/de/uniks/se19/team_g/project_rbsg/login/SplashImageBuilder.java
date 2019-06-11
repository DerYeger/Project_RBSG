package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class SplashImageBuilder {

    private BackgroundImage backgroundImage;

    public BackgroundImage getSplashImage() {
        if (backgroundImage == null) {
            final String url = SplashImageBuilder.class.getResource("splash.jpg").toString();

            final Image image = new Image(url,
                    ProjectRbsgFXApplication.WIDTH,
                    ProjectRbsgFXApplication.HEIGHT,
                    true,
                    true);

            backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
        }

        return backgroundImage;
    }
}
