package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/**
 * @author  Keanu Stückrad
 */
public class UnitPropertyInfoListController {

    public Button closeButton;
    public VBox infoBox;

    private Node root;

    private UnitPropertyInfoCellBuilder health;
    // private UnitPropertyInfoCellBuilder physicalResistance;
    // private UnitPropertyInfoCellBuilder magicResistance;
    // private UnitPropertyInfoCellBuilder speed;
    // private UnitPropertyInfoCellBuilder attack;
    // private UnitPropertyInfoCellBuilder spellPower;

    public void init() {
        JavaFXUtils.setButtonIcons(
                closeButton,
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                40
        );
        initBuilders();
        infoBox.getChildren().addAll(
                health.buildInfoCellNode(new Image("/assets/icons/army/magicDefense.png"), "magicDefense")
                // More Properties
        );
    }

    private void initBuilders() {
        health = new UnitPropertyInfoCellBuilder();
        // physicalResistance = new UnitPropertyInfoCellBuilder();
        // magicResistance = new UnitPropertyInfoCellBuilder();
        // speed = new UnitPropertyInfoCellBuilder();
        // attack = new UnitPropertyInfoCellBuilder();
        // spellPower = new UnitPropertyInfoCellBuilder();
    }

    public void setRootNode(Node root){
        this.root = root;
    }

    public void closeInfo(ActionEvent actionEvent) {
        if (root != null){
            root.setVisible(false);
        }
    }

}
