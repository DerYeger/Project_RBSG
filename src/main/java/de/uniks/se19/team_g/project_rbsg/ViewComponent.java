package de.uniks.se19.team_g.project_rbsg;

import org.w3c.dom.Node;

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
}
