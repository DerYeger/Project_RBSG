package de.uniks.se19.team_g.project_rbsg.lobby.loading_screen;

import io.rincl.Rincled;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

@Component
public class LoadingScreenController implements Rincled {

    public Label loadingText;
    public ImageView loadingGif;

    private String tip;

    public void init() {
        Image gif = new Image(getClass().getResource("/assets/gifs/Ring.gif").toExternalForm());
        loadingGif.setImage(gif);
        updateLabels();
    }

    public void updateLabels(){
        double random = Math.random();
        if (random < 0.3){
            loadingText.textProperty().setValue(getResources().getString("firstTip"));
        } else if (random >= 0.3 && random < 0.6){
            loadingText.textProperty().setValue(getResources().getString("secondTip"));
        } else {
            loadingText.textProperty().setValue(getResources().getString("thirdTip"));
        }
    }

    private void chooseTip(){

    }

}
