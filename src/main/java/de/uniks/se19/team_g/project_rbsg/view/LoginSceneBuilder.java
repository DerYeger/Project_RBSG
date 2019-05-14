package de.uniks.se19.team_g.project_rbsg.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public class LoginSceneBuilder {

    private Scene loginScene;

    private SplashImageBuilder splashImageBuilder;
    private LoginFormBuilder loginFormBuilder;

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
