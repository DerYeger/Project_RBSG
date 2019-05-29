package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.CreateGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author Juri Lozowoj
 */
@Component
public class CreateGameFormBuilder {

    private Node createGameForm;

    private FXMLLoader fxmlLoader;

    @Autowired
    public CreateGameFormBuilder(FXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
    }

    public Node getCreateGameForm() throws IOException {
        if(this.createGameForm == null){
            fxmlLoader.setLocation(CreateGameFormBuilder.class.getResource("create-game-popup.fxml"));
            createGameForm = fxmlLoader.load();
            final CreateGameController createGameController = fxmlLoader.getController();
            createGameController.init();
        }
        return createGameForm;
    }
}
