package de.uniks.se19.team_g.project_rbsg;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class ViewComponent<T> {
    private final Node root;

    public Node getRoot() {
        return root;
    }

    public T getController() {
        return controller;
    }

    private final T controller;

    public ViewComponent(Node root, T controller) {

        this.root = root;
        this.controller = controller;
    }

    public static <T> ViewComponent<T> fromLoader(FXMLLoader loader)
    {
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ViewComponent<>(loader.getRoot(), loader.getController());
    }
}
