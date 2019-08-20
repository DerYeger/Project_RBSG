package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_army;

import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.fxml.*;
import javafx.scene.*;

import java.io.*;

public class ArmyPreviewBuilder
{

    public ArmyPreviewController getLastController()
    {
        return lastController;
    }

    private ArmyPreviewController lastController;

    public Node build (Army army)
    {
        Node node = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ui/waiting_room/ArmyPreviewView.fxml"));
        try
        {
            node = fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        lastController = fxmlLoader.getController();

        lastController.init(army);

        return node;
    }
}
