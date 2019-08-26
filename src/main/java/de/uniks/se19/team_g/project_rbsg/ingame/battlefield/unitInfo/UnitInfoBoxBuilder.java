package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Hoverable;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Selectable;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Locale;

public class UnitInfoBoxBuilder<T>
{

    private Node unitInfoBox;
    private FXMLLoader fxmlLoader;
    private UnitInfoBoxController<T> lastController;
    private String status;
    private Property<Locale> selectedLocale;

    public Node build(ObservableObjectValue<? extends T> bindable, String status, Property<Locale> selectedLocale)
    {
        this.selectedLocale = selectedLocale;
        this.status = status;
        createNode();

        UnitInfoBoxController controller = fxmlLoader.getController();
        controller.bindUnit(bindable);

        return unitInfoBox;
    }


    private void createNode()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/ui/ingame/unitInfoBox.fxml"));
        lastController = new UnitInfoBoxController<T>(status, selectedLocale);
        fxmlLoader.setController(lastController);
        try
        {
            unitInfoBox = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public UnitInfoBoxController<T> getLastController()
    {
        return lastController;
    }

}
