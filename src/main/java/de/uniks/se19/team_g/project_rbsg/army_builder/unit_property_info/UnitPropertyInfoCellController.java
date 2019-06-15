package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UnitPropertyInfoCellController {

    public ImageView imageView;
    public Label infoLabel;

    public void init(Image propertyImage, File propertyInfo) {
        imageView.setImage(propertyImage);
        String data = null;
        try {
            data = new String(Files.readAllBytes(Paths.get(getClass().getResource(propertyInfo.getAbsolutePath()).toString().substring(5))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        infoLabel.setText(data);
    }

}
