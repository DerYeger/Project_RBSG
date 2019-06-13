package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class UnitPropertyInfoCellBuilder {

    private Node infoCellView;
    private FXMLLoader fxmlLoader;

    public Node buildInfoCellNode() {
        if(infoCellView == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/army_builder/unit-property-info-cell-view.fxml"));
            try {
                infoCellView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final UnitPropertyInfoCellController unitPropertyInfoCellController = fxmlLoader.getController();
            unitPropertyInfoCellController.init();
        }
        return infoCellView;
    }

}
