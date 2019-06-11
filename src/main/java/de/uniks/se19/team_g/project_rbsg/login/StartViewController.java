package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Keanu Stückrad
 */
@Controller
@Scope("prototype")
public class StartViewController {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView musicImage;
    @FXML
    private VBox loginAndTitleBox;

    private boolean musicRunning = true;
    private final LoginFormBuilder loginFormBuilder;
    private final TitleViewBuilder titleViewBuilder;
    private final SplashImageBuilder splashImageBuilder;
    private final SceneManager sceneManager;

    @Autowired
    public StartViewController(@NotNull final SplashImageBuilder splashImageBuilder, @NotNull final LoginFormBuilder loginFormBuilder, @NotNull final TitleViewBuilder titleViewBuilder, @NotNull final SceneManager sceneManager) {
        this.splashImageBuilder = splashImageBuilder;
        this.loginFormBuilder = loginFormBuilder;
        this.titleViewBuilder = titleViewBuilder;
        this.sceneManager = sceneManager;
    }

    public void init() throws IOException {
        setButtonIcon("/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_black_48dp.png");
        root.setBackground(new Background(splashImageBuilder.getSplashImage()));
        loginAndTitleBox.getChildren().addAll(titleViewBuilder.getTitleForm(), loginFormBuilder.getLoginForm());
    }

    public void toggleSound(ActionEvent actionEvent) {
        musicRunning = !musicRunning;
        updateMusicButtonIcons();
    }

    private void updateMusicButtonIcons() {
        if(musicRunning) {
            setButtonIcon("/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_black_48dp.png");
            sceneManager.playAudio();
        } else {
            setButtonIcon("/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_black_48dp.png");
            sceneManager.stopAudio();
        }
    }

    private void setButtonIcon(String iconName) {
        Platform.runLater(() -> musicImage.setImage(new Image(String.valueOf(getClass().getResource(iconName)))));
    }

}
