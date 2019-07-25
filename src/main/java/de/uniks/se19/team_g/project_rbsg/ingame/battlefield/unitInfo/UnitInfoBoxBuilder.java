package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class UnitInfoBoxBuilder
{

    private Node unitInfoBox;
    private FXMLLoader fxmlLoader;

    public Node build(ObjectProperty<Unit> unit)
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/ui/ingame/unitInfoBox.fxml"));
        try
        {
            unitInfoBox = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        UnitInfoBoxController controller = fxmlLoader.getController();
        controller.bindUnit(unit);

        return unitInfoBox;
    }
}
