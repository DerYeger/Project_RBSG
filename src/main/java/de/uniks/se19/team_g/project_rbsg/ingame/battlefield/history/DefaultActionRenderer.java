package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;

public abstract class DefaultActionRenderer implements ActionRenderer {

    protected ObjectFactory<FXMLLoader> loaderFactory;

    public DefaultActionRenderer(@Qualifier("fxmlLoader") ObjectFactory<FXMLLoader> loaderFactory) {
        this.loaderFactory = loaderFactory;
    }

    protected FXMLLoader getFXMLLoader() {
        FXMLLoader fxmlLoader = loaderFactory.getObject();
        fxmlLoader.setLocation(getFxmlUrl());

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fxmlLoader;
    }

    protected Pair<DefaultHistoryCellController, HBox> loadCell() {
        FXMLLoader fxmlLoader = getFXMLLoader();

        return new Pair<>(fxmlLoader.getController(), fxmlLoader.getRoot());
    }

    protected URL getFxmlUrl() {
        return getClass().getResource("/ui/ingame/battleField/defaultHistoryCell.fxml");
    }

    @Override
    public HistoryRenderData render(Action action) {

        if (!supports(action)) return null;

        return doRender(action);
    }

    @Nonnull
    protected abstract HistoryRenderData doRender(Action action);
}
