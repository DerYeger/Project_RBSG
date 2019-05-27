package de.uniks.se19.team_g.project_rbsg.Login_Registration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Keanu Stückrad
 */

@Component
public class TitleFormBuilder {

    private Node titleForm;

    private FXMLLoader fxmlLoader;

    @Autowired
    public TitleFormBuilder(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Node getTitleForm() throws IOException {
        if (titleForm == null) {
            fxmlLoader.setLocation(TitleFormBuilder.class.getResource("title-form.fxml"));
            titleForm = fxmlLoader.load();
            final TitleFormController titleFormController = fxmlLoader.getController();
            titleFormController.init();
        }
        return titleForm;
    }

}
