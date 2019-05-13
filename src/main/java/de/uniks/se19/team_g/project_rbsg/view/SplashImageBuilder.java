package de.uniks.se19.team_g.project_rbsg.view;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class SplashImageBuilder {

    private static BackgroundImage backgroundImage;

    public static BackgroundImage getSplashImage() {
        if (backgroundImage == null) {
            final String url = SplashImageBuilder.class.getResource("splash.jpg").toString();

            final Image image = new Image(url,
                    1336,
                    768,
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
