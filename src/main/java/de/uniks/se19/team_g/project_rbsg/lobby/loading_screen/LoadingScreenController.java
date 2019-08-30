package de.uniks.se19.team_g.project_rbsg.lobby.loading_screen;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincl;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

@Component
public class LoadingScreenController implements Rincled {

    public Label loadingText;
    public ImageView loadingGif;

    public void init() {
        Image gif = new Image(getClass().getResource("/assets/gifs/Ring.gif").toExternalForm());
        loadingGif.setImage(gif);
        updateLabels();
    }

    public void updateLabels(){
        Random random = new Random();
        int i = random.nextInt(3);
        if (i == 0){
            loadingText.setText(Rincl.getResources(ProjectRbsgFXApplication.class).getString("firstTip"));
        } else if (i == 1){
            loadingText.setText(Rincl.getResources(ProjectRbsgFXApplication.class).getString("secondTip"));
        } else {
            loadingText.setText(Rincl.getResources(ProjectRbsgFXApplication.class).getString("thirdTip"));
        }
    }
}
