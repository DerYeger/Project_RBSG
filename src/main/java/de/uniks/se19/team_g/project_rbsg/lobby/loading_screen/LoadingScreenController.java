package de.uniks.se19.team_g.project_rbsg.lobby.loading_screen;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

@Component
public class LoadingScreenController {

    public Label loadingText;
    public ImageView loadingGif;

    public void init() {
        //Image gif = new Image(String.valueOf(new File(LoadingScreenController.class.getResource("/assets/gifs/Ring.gif").toString())));
        Image gif = new Image(getClass().getResource("/assets/gifs/Ring.gif").toExternalForm());
        loadingGif.setImage(gif);
    }

}
