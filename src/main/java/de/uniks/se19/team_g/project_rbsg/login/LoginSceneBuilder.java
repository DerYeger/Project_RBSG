package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.login.LoginFormBuilder;
import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Jan Müller
 * Returns a Scene using a StackPane as the root node.
 * The StackPane uses the background provided by the passed SplashImageBuilder.
 * Its only child is the Node provided by the LoginFormBuilder.
 */
@Component
public class LoginSceneBuilder {

    private Scene loginScene;

    private SplashImageBuilder splashImageBuilder;
    private LoginFormBuilder loginFormBuilder;

    @Autowired
    public LoginSceneBuilder(@NotNull final SplashImageBuilder splashImageBuilder, @NotNull final LoginFormBuilder loginFormBuilder) {
        this.splashImageBuilder = splashImageBuilder;
        this.loginFormBuilder = loginFormBuilder;
    }

    public Scene getLoginScene() throws IOException {
        if (loginScene == null) {
            final StackPane pane = new StackPane();
            pane.setBackground(new Background(splashImageBuilder.getSplashImage()));
            pane.setAlignment(Pos.CENTER);
            pane.getChildren().add(loginFormBuilder.getLoginForm());

            loginScene = new Scene(pane);
        }
        return loginScene;
    }
}
