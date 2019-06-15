package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import de.uniks.se19.team_g.project_rbsg.configuration.ButtonIconsSetter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.File;

public class UnitPropertyInfoListController extends ButtonIconsSetter {

    public Button closeButton;
    public VBox infoBox;

    private Node root;

    private UnitPropertyInfoCellBuilder health;
    private UnitPropertyInfoCellBuilder physicalResistance;
    private UnitPropertyInfoCellBuilder magicResistance;
    private UnitPropertyInfoCellBuilder speed;
    private UnitPropertyInfoCellBuilder attack;
    private UnitPropertyInfoCellBuilder spellPower;

    public void init() {
        setButtonIcons(closeButton, "/assets/icons/navigation/arrow-back-black.png", "/assets/icons/navigation/arrow-back-white.png", 40);
        initBuilders();
        // add property images and infos here with relative path
        infoBox.getChildren().addAll(
                health.buildInfoCellNode(new Image("/assets/icons/army/magic-defense.png"), new File("/assets/text_files/magicDefense.txt")),
                physicalResistance.buildInfoCellNode(new Image("/assets/icons/army/magic-defense.png"), new File("/assets/text_files/magicDefense.txt")),
                magicResistance.buildInfoCellNode(new Image("/assets/icons/army/magic-defense.png"), new File("/assets/text_files/magicDefense.txt")),
                speed.buildInfoCellNode(new Image("/assets/icons/army/magic-defense.png"), new File("/assets/text_files/magicDefense.txt")),
                attack.buildInfoCellNode(new Image("/assets/icons/army/magic-defense.png"), new File("/assets/text_files/magicDefense.txt")),
                spellPower.buildInfoCellNode(new Image("/assets/icons/army/magic-defense.png"), new File("/assets/text_files/magicDefense.txt"))
        );
    }

    private void initBuilders() {
        health = new UnitPropertyInfoCellBuilder();
        physicalResistance = new UnitPropertyInfoCellBuilder();
        magicResistance = new UnitPropertyInfoCellBuilder();
        speed = new UnitPropertyInfoCellBuilder();
        attack = new UnitPropertyInfoCellBuilder();
        spellPower = new UnitPropertyInfoCellBuilder();
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
