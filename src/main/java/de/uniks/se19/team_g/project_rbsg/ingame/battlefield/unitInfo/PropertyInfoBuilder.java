package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.Nullable;

import java.io.IOException;

public class PropertyInfoBuilder
{
    private Node propertyInfoNode;
    private FXMLLoader fxmlLoader;

    public Node build(@Nullable final String imagePath, @Nullable final StringProperty propertyText)
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/ui/ingame/propertyInfo.fxml"));
        try
        {
            propertyInfoNode = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        PropertyInfoController controller = fxmlLoader.getController();
        controller.setProperty(imagePath, propertyText);

        return propertyInfoNode;
    }
}
