package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 */
@Component
public class IngameSceneBuilder {

    @Autowired
    private IngameViewBuilder ingameViewBuilder;

    @NonNull
    public Scene getIngameScene() throws Exception {
        Node ingameViewNode = ingameViewBuilder.buildIngameView();
        return new Scene((Parent) ingameViewNode);
    }
}
