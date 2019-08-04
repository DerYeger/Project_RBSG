package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertyInfoController implements Initializable
{
    public HBox propertyContainer;
    public ImageView propertyIcon;
    public Label propertyLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void setProperty(String imagePath, StringProperty propertyText) {
        Image image = new Image(imagePath, 40 , 40, false, true);
        propertyIcon.setImage(image);

        propertyLabel.textProperty().bind(propertyText);
    }
}
