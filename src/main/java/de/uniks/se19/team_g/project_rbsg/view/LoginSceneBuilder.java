package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.Login_Registration.TitleFormBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Jan Müller
 * @edited Keanu Stückrad
 * Returns a Scene using a VBox as the root node.
 * The VBox uses the background provided by the passed SplashImageBuilder.
 * Its children are the Nodes provided by the LoginFormBuilder and TitleFormBuilder.
 */
@Component
public class LoginSceneBuilder {

    private Scene loginScene;

    private SplashImageBuilder splashImageBuilder;
    private LoginFormBuilder loginFormBuilder;
    private TitleFormBuilder titleFormBuilder;

    @Autowired
    public LoginSceneBuilder(@NotNull final SplashImageBuilder splashImageBuilder, @NotNull final LoginFormBuilder loginFormBuilder,  @NotNull final TitleFormBuilder titleFormBuilder) {
        this.splashImageBuilder = splashImageBuilder;
        this.loginFormBuilder = loginFormBuilder;
        this.titleFormBuilder = titleFormBuilder;
    }

    public Scene getLoginScene() throws IOException {
        if (loginScene == null) {
            final VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(titleFormBuilder.getTitleForm(), loginFormBuilder.getLoginForm());
            vBox.setBackground(new Background(splashImageBuilder.getSplashImage()));
            loginScene = new Scene(vBox);
        }
        return loginScene;
    }
}
