package de.uniks.se19.team_g.project_rbsg.lobby.loading_screen;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
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

    private String tip;

    private final Property<Locale> selectedLocale;

    @Autowired
    public LoadingScreenController(@Nonnull final Property<Locale> selectedLocale) {
        this.selectedLocale = selectedLocale;
    }

    public void init() {
        Image gif = new Image(getClass().getResource("/assets/gifs/Ring.gif").toExternalForm());
        loadingGif.setImage(gif);
        bindLabels();
    }

    public void bindLabels(){
        Random random = new Random();
        int i = random.nextInt(3);
        if (i == 0){
            loadingText.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "firstTip"));
        } else if (i == 1){
            loadingText.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "secondTip"));
        } else {
            loadingText.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "thirdTip"));
        }
    }

    private void chooseTip(){

    }
}
