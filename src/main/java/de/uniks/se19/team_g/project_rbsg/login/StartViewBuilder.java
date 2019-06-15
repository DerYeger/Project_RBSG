package de.uniks.se19.team_g.project_rbsg.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * @author Keanu Stückrad
 */
@Component
@Scope("prototype")
public class StartViewBuilder {

    private Node startView;

    private FXMLLoader fxmlLoader;

    @Autowired
    public StartViewBuilder(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Node getStartView() throws IOException {
        if (startView == null) {
            fxmlLoader.setLocation(LoginFormBuilder.class.getResource("/ui/login/startView.fxml"));
            startView = fxmlLoader.load();
            final StartViewController startViewController = fxmlLoader.getController();
            startViewController.init();
        }
        return startView;
    }

}
