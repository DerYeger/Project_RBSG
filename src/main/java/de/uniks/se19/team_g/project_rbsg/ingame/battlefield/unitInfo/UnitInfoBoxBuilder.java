package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Hoverable;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Selectable;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class UnitInfoBoxBuilder<T>
{

    private Node unitInfoBox;
    private FXMLLoader fxmlLoader;

    public Node build(ObservableObjectValue<? extends T> bindable)
    {
        createNode();

        UnitInfoBoxController controller = fxmlLoader.getController();
        controller.bindUnit(bindable);

        return unitInfoBox;
    }


    private void createNode()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/ui/ingame/unitInfoBox.fxml"));
        fxmlLoader.setController(new UnitInfoBoxController<T>());
        try
        {
            unitInfoBox = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
