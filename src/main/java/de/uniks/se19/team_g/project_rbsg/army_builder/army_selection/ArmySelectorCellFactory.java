package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.beans.property.*;
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
    private SimpleObjectProperty<Army> hoveredArmy = null;

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
        ArmySelectorCellController controller = armySelectorCellController.getObject();
        loader.setController(controller);
        loader.setLocation(getClass().getResource("/ui/army_builder/armySelectorCell.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controller.setHoverProperty(hoveredArmy);

        return loader.getController();
    }

    public void setArmyHoverProperty (SimpleObjectProperty<Army> armyHoverProperty)
    {
        this.hoveredArmy  = armyHoverProperty;
    }
}
