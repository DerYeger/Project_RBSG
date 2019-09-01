package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HistoryViewProvider {


    private final ObjectFactory<FXMLLoader> loaderFactory;

    public HistoryViewProvider(ObjectProvider<FXMLLoader> loaderFactory) {
        this.loaderFactory = loaderFactory;
    }

    private FXMLLoader getLoader() {
        FXMLLoader loader = loaderFactory.getObject();
        loader.setLocation(getClass().getResource("/ui/ingame/battleField/historyView.fxml"));
        return loader;
    }

    public HistoryViewController mountHistory(
            VBox root,
            IngameContext context
    ) {
        FXMLLoader loader = getLoader();
        loader.setRoot(root);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HistoryViewController controller = loader.getController();
        controller.configureContext(context);
        return controller;
    }
}
