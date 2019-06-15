package de.uniks.se19.team_g.project_rbsg.lobby.game;

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

    private CreateGameController createGameController;

    @Autowired
    public CreateGameFormBuilder(FXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
    }

    public Node getCreateGameForm() throws IOException {
        if(this.createGameForm == null){
            fxmlLoader.setLocation(CreateGameFormBuilder.class.getResource("/ui/lobby/create_game/createGame.fxml"));
            createGameForm = fxmlLoader.load();
            createGameController = fxmlLoader.getController();
            createGameController.init();
            createGameController.setRootNode(createGameForm);
        }
        createGameForm.setVisible(true);
        return createGameForm;
    }

    public CreateGameController getCreateGameController()
    {
        return createGameController;
    }

    public void setCreateGameController(CreateGameController createGameController)
    {
        this.createGameController = createGameController;
    }
}
