package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ArmySelectorCellFactory implements Callback<ListView<Army>, ListCell<Army>> {

    private final ObjectFactory<FXMLLoader> fxmlLoader;
    private final ObjectFactory<ArmySelectorCellController> armySelectorCellController;

    public ArmySelectorCellFactory(
            ObjectFactory<FXMLLoader> fxmlLoader,
            ObjectFactory<ArmySelectorCellController> armySelectorCellController
    ) {
        this.fxmlLoader = fxmlLoader;
        this.armySelectorCellController = armySelectorCellController;
    }

    @Override
    public ListCell<Army> call(ListView<Army> param) {
        final FXMLLoader loader = fxmlLoader.getObject();
        loader.setController(armySelectorCellController.getObject());
        loader.setLocation(getClass().getResource("/ui/army_builder/armySelectorCell.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loader.getController();
    }
}
