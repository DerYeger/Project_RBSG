package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import de.uniks.se19.team_g.project_rbsg.army_builder.SceneController;
import de.uniks.se19.team_g.project_rbsg.configuration.ButtonIconsSetter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.springframework.lang.NonNull;

public class UnitPropertyInfoListController extends ButtonIconsSetter {

    private static final int PROPERTY_COUNT = 7;

    public Button closeButton;
    public VBox infoBox;

    private UnitPropertyInfoCellBuilder unitPropertyInfoCellBuilder;

    private final SceneController sceneController;

    public UnitPropertyInfoListController(@NonNull final SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void init() {
        setButtonIcons(closeButton, "/assets/icons/navigation/arrow-back-black.png", "/assets/icons/navigation/arrow-back-white.png", 40);
        unitPropertyInfoCellBuilder = new UnitPropertyInfoCellBuilder();
        for (int i = 0; i < PROPERTY_COUNT; i++) {
            infoBox.getChildren().add(unitPropertyInfoCellBuilder.buildInfoCellNode());
        }
    }

    public void closeInfo(ActionEvent actionEvent) {
        sceneController.infoFlag.set(false);
    }

}
