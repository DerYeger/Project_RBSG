package de.uniks.se19.team_g.project_rbsg;

import javafx.fxml.FXMLLoader;
import org.w3c.dom.Node;

import java.io.IOException;

public class ViewComponent {
    private final Node root;

    public Node getRoot() {
        return root;
    }

    public Object getController() {
        return controller;
    }

    private final Object controller;

    public ViewComponent(Node root, Object controller) {

        this.root = root;
        this.controller = controller;
    }

    public static ViewComponent fromLoader(FXMLLoader loader)
    {
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ViewComponent(loader.getRoot(), loader.getController());
    }
}
