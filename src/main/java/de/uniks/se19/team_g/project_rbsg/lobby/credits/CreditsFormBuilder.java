package de.uniks.se19.team_g.project_rbsg.lobby.credits;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Juri Lozowoj
 */
@Component
public class CreditsFormBuilder {

    private Node creditsForm;

    private FXMLLoader fxmlLoader;

    private CreditsController creditsController;

    @Autowired
    public CreditsFormBuilder(FXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
    }

    public Node getCreditsForm(String board, String icons, String music, String units, String frameworks) throws IOException{
        if (this.creditsForm == null){
            fxmlLoader.setLocation(CreditsFormBuilder.class.getResource("/ui/lobby/credits/credits.fxml"));
            creditsForm = fxmlLoader.load();
            //init missing so far
            creditsController = fxmlLoader.getController();
            creditsController.setRootNode(creditsForm);
            creditsController.init(board, icons, music, units, frameworks);
        }
        creditsForm.setVisible(true);
        return creditsForm;
    }


    public CreditsController getCreditsController() {
        return creditsController;
    }

    public void setCreditsController(CreditsController creditsController) {
        this.creditsController = creditsController;
    }
}
