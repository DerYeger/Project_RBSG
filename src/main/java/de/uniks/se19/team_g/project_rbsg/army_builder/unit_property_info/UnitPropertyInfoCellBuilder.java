package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
public class UnitPropertyInfoCellBuilder {

    private Node infoCellView;
    private FXMLLoader fxmlLoader;

    public Node buildInfoCellNode(Image propertyImage, String propertyInfo) {
        if(infoCellView == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/army_builder/unitPropertyInfoCellView.fxml"));
            try {
                infoCellView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final UnitPropertyInfoCellController unitPropertyInfoCellController = fxmlLoader.getController();
            unitPropertyInfoCellController.init(propertyImage, propertyInfo);
        }
        return infoCellView;
    }

}
