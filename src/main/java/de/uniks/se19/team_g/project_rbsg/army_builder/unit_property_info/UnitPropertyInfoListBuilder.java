package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Locale;

/**
 * @author  Keanu Stückrad
 */
public class UnitPropertyInfoListBuilder {

    private Node infoView;
    private FXMLLoader fxmlLoader;

    public Node buildInfoView(Property<Locale> selectedLocale) {
        if(infoView == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/ui/army_builder/unitPropertyInfoListView.fxml"));
            try {
                infoView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final UnitPropertyInfoListController unitPropertyInfoListController = fxmlLoader.getController();
            unitPropertyInfoListController.setRootNode(infoView);
            unitPropertyInfoListController.init(selectedLocale);
        }
        return infoView;
    }

}
