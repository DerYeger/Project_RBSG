package de.uniks.se19.team_g.project_rbsg.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class ViewComponent<T> {

    private static Logger logger = LoggerFactory.getLogger(ViewComponent.class);

    private final Node root;

    public <R extends Node> R getRoot() {
        return (R) root;
    }

    public T getController() {
        return controller;
    }

    private final T controller;

    public ViewComponent(Node root, T controller) {

        this.root = root;
        this.controller = controller;
    }

    public static <T> ViewComponent<T> fromLoader(@NonNull final FXMLLoader loader)
    {
        try {
            loader.load();
        } catch (IOException e) {
            logger.error("failed to load component", e);
            throw new RuntimeException(e);
        }

        return new ViewComponent<>(loader.getRoot(), loader.getController());
    }
}
