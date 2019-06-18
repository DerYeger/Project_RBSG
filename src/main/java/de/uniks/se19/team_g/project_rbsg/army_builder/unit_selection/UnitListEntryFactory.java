package de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnitListEntryFactory implements Callback<ListView<Unit>, ListCell<Unit>> {

    final FXMLLoaderFactory fxmlLoaderFactory;

    public UnitListEntryFactory(final FXMLLoaderFactory fxmlLoaderFactory) {
        this.fxmlLoaderFactory = fxmlLoaderFactory;
    }

    @Override
    public ListCell<Unit> call(ListView<Unit> param) {
        final FXMLLoader loader = fxmlLoaderFactory.fxmlLoader();
        loader.setLocation(getClass().getResource("/ui/army_builder/unitListEntry.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loader.getController();
    }
}
