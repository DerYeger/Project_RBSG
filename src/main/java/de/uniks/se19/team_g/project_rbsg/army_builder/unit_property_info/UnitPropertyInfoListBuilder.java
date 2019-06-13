package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class UnitPropertyInfoListBuilder {

    private Node infoView;
    private FXMLLoader fxmlLoader;

    public Node buildInfoView() {
        if(infoView == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/army_builder/unit-property-info-list-view.fxml"));
            try {
                infoView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final UnitPropertyInfoListController unitPropertyInfoListController = fxmlLoader.getController();
            unitPropertyInfoListController.init();
        }
        return infoView;
    }

}
