package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.scene.RootController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Keanu Stückrad
 */
@Controller
@Scope("prototype")
public class StartViewController implements RootController {

    public StackPane mainPane;
    public AnchorPane root;
    public Button musicButton;
    public VBox loginAndTitleBox;

    private final LoginFormBuilder loginFormBuilder;
    private final TitleViewBuilder titleViewBuilder;
    private final SplashImageBuilder splashImageBuilder;
    private final MusicManager musicManager;

    @Autowired
    public StartViewController(@NotNull final SplashImageBuilder splashImageBuilder,
                               @NotNull final LoginFormBuilder loginFormBuilder,
                               @NotNull final TitleViewBuilder titleViewBuilder,
                               @NotNull final MusicManager musicManager,
                               @NonNull final LoginFormController loginFormController) {
        this.splashImageBuilder = splashImageBuilder;
        this.loginFormBuilder = loginFormBuilder;
        this.titleViewBuilder = titleViewBuilder;
        this.musicManager = musicManager;
    }

    public void initialize() throws IOException {
        root.setBackground(new Background(splashImageBuilder.getSplashImage()));
        loginAndTitleBox.getChildren().addAll(titleViewBuilder.getTitleForm(), loginFormBuilder.getLoginForm());
        musicManager.initButtonIcons(musicButton);
    }

    public void toggleSound(ActionEvent actionEvent) {
        musicManager.toggleMusicAndUpdateButtonIconSet(musicButton);
    }
}
