package de.uniks.se19.team_g.project_rbsg.lobby.credits;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.springframework.lang.Nullable;
/**
 * @author Juri Lozowoj
 */
public class CreditsController {

    private Node root;

    public void setRootNode(Node root){
        this.root = root;
    }

    public Node getRoot(){
        return this.root;
    }

    public void hideCredits(@Nullable ActionEvent event){
        if (root != null) {
            root.setVisible(false);
        }
    }
}
