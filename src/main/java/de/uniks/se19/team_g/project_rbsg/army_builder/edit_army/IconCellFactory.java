package de.uniks.se19.team_g.project_rbsg.army_builder.edit_army;

import de.uniks.se19.team_g.project_rbsg.configuration.ArmyIcon;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.util.Callback;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IconCellFactory implements Callback<ListView<ArmyIcon>, ListCell<ArmyIcon>> {

    private final ObjectFactory<FXMLLoader> fxmlLoader;
    private final ObjectFactory<IconCellController> iconCellController;

    public IconCellFactory(
        ObjectFactory<FXMLLoader> fxmlLoader,
        ObjectFactory<IconCellController> iconCellController
    ) {
        this.fxmlLoader = fxmlLoader;
        this.iconCellController = iconCellController;
    }

    @Override
    public ListCell<ArmyIcon> call(ListView<ArmyIcon> param) {
        final FXMLLoader loader = fxmlLoader.getObject();
        loader.setController(iconCellController.getObject());
        loader.setLocation(getClass().getResource("/ui/army_builder/armySelectorCell.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loader.getController();
    }
}