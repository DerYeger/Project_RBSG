package de.uniks.se19.team_g.project_rbsg.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Keanu Stückrad
 */

@Component
public class TitleViewBuilder {

    private Node titleView;

    private FXMLLoader fxmlLoader;

    @Autowired
    public TitleViewBuilder(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Node getTitleForm() throws IOException {
        if (titleView == null) {
            fxmlLoader.setLocation(TitleViewBuilder.class.getResource("title-form.fxml"));
            titleView = fxmlLoader.load();
            final TitleViewController titleFormController = fxmlLoader.getController();
            titleFormController.init();
        }
        return titleView;
    }

}
