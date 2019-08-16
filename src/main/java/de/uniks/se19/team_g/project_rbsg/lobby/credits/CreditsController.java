package de.uniks.se19.team_g.project_rbsg.lobby.credits;

import io.rincl.Rincled;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

@Controller
public class CreditsController implements Rincled {

    private Node root;
    public Label boardLabel;
    public Label iconLabel;
    public Label musicLabel;
    public Label unitLabel;
    public Label frameworkLabel;

    private String board;
    private String icons;
    private String music;
    private String units;
    private String frameworks;


    public void init(@NonNull String board, @NonNull String icons, @NonNull String music, @NonNull String units, @NonNull String frameworks){
        this.board = board;
        this.icons = icons;
        this.music = music;
        this.units = units;
        this.frameworks = frameworks;

        updateLabels();
    }

    public void updateLabels(){
        boardLabel.textProperty().setValue(getResources().getString(board));
        iconLabel.textProperty().setValue(getResources().getString(icons));
        musicLabel.textProperty().setValue(getResources().getString(music));
        unitLabel.textProperty().setValue(getResources().getString(units));
        frameworkLabel.textProperty().setValue(getResources().getString(frameworks));
    }


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