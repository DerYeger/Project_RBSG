package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
@Component
public class IngameViewBuilder {

    private IngameViewController ingameViewController;
    private FXMLLoaderFactory fxmlLoaderFactory;
    private Node ingameView;

    public IngameViewBuilder(FXMLLoaderFactory fxmlLoaderFactory) {
        this.fxmlLoaderFactory = fxmlLoaderFactory;
    }

    private FXMLLoader getLoader() {
        FXMLLoader loader = fxmlLoaderFactory.fxmlLoader();
        loader.setLocation(getClass().getResource("/ui/ingame/ingameView.fxml"));
        return loader;
    }

    public @NonNull
    Node buildIngameView() {
        FXMLLoader fxmlLoader = getLoader();
        try {
            ingameView = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ingameViewController = fxmlLoader.getController();
        ingameViewController.init();
        return ingameView;
    }

}
