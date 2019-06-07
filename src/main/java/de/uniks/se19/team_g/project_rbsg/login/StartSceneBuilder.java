package de.uniks.se19.team_g.project_rbsg.login;

import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * @author Jan Müller
 * @edited Keanu Stückrad
 */
@Component
@Scope("prototype")
public class StartSceneBuilder {

    private Scene startScene;

    private final StartViewBuilder startViewBuilder;

    @Autowired
    public StartSceneBuilder(@NonNull final StartViewBuilder startViewBuilder) {
        this.startViewBuilder = startViewBuilder;
    }

    public Scene getStartScene() throws IOException {
        if (startScene == null) {
            startScene = new Scene((Parent) startViewBuilder.getStartView());
        }
        return startScene;
    }
}
