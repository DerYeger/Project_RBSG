package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.Locale;

/**
 * @author  Keanu Stückrad
 */
public class UnitPropertyInfoListController {

    public Button closeButton;
    public VBox infoBox;

    private Node root;

    public void init(Property<Locale> selectedLocale) {
        JavaFXUtils.setButtonIcons(
                closeButton,
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                40
        );
        UnitPropertyInfoCellBuilder health = new UnitPropertyInfoCellBuilder();
        UnitPropertyInfoCellBuilder movement = new UnitPropertyInfoCellBuilder();
        UnitPropertyInfoCellBuilder canAttack = new UnitPropertyInfoCellBuilder();
        infoBox.getChildren().addAll(
                health.buildInfoCellNode(selectedLocale, new Image("/assets/icons/units/hpIcon.png"), "health"),
                movement.buildInfoCellNode(selectedLocale, new Image("/assets/icons/units/mpIcon.png"),  "movement"),
                canAttack.buildInfoCellNode(selectedLocale, new Image("/assets/icons/operation/swordClashWhite.png"), "canAttack")
        );
    }

    protected void setRootNode(Node root){
        this.root = root;
    }

    public void closeInfo(ActionEvent actionEvent) {
        if (root != null){
            root.setVisible(false);
        }
    }

}
