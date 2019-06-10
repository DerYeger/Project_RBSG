package de.uniks.se19.team_g.project_rbsg.army_builder;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class SceneController implements Initializable {

    private final SimpleObjectProperty<Context> contextProvider;
    public VBox sideBarLeft;
    public VBox content;
    public HBox topContentContainer;
    public ScrollPane unitListView;
    public Pane unitDetailView;
    public VBox armyView;
    public VBox sideBarRight;
    public HBox armyBuilderScene;

    public SceneController(SimpleObjectProperty<Context> contextProvider)
    {
        this.contextProvider = contextProvider;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (contextProvider.get() == null) {
            contextProvider.set(new Context());
        }
    }
}
