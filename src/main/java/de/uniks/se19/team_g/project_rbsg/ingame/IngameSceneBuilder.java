package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
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

    @Autowired
    private SplashImageBuilder splashImageBuilder;

    private Scene ingameScene;

    @NonNull
    public Scene getIngameScene() throws Exception {
        if (ingameScene == null) {
            Node ingameNode = ingameViewBuilder.buildIngameView();
            final AnchorPane parent = new AnchorPane(ingameNode);
            parent.setBackground(new Background(splashImageBuilder.getSplashImage()));
            AnchorPane.setBottomAnchor(ingameNode, 0.0);
            AnchorPane.setLeftAnchor(ingameNode, 0.0);
            AnchorPane.setRightAnchor(ingameNode, 0.0);
            AnchorPane.setTopAnchor(ingameNode, 0.0);
            ingameScene = new Scene(parent);
        }
        return ingameScene;
    }
}
