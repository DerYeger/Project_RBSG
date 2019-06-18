package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;

@Component
public class ArmySquadCellFactory implements Callback<ListView<Unit>, ListCell<Unit>> {

    @Nonnull
    private final ObjectFactory<FXMLLoader> loaderFactory;

    public  ArmySquadCellFactory(
        @Nonnull ObjectFactory<FXMLLoader> loaderFactory) {
        this.loaderFactory = loaderFactory;
    }

    @Override
    public ListCell<Unit> call(ListView<Unit> param) {
        final FXMLLoader loader = loaderFactory.getObject();
        loader.setLocation(getClass().getResource("/ui/army_builder/ArmySquad.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loader.getController();
    }
}
