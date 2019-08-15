package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.ActionImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class MovementActionRenderer implements ActionRenderer {

    private ObjectFactory<FXMLLoader> loaderFactory;

    public MovementActionRenderer(
            @Qualifier("fxmlLoader") ObjectFactory<FXMLLoader> loaderFactory
    ) {
        this.loaderFactory = loaderFactory;
    }

    private FXMLLoader getFXMLLoader() {
        FXMLLoader fxmlLoader = loaderFactory.getObject();
        fxmlLoader.setLocation(getFxmlUrl());

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fxmlLoader;
    }

    private URL getFxmlUrl() {
        return getClass().getResource("/ui/ingame/battleField/defaultHistoryCell.fxml");
    }

    @Override
    public Node render(Action action) {

        if (!supports(action)) return null;

        ActionImpl actionImpl = (ActionImpl) action;
        Unit unit = (Unit) actionImpl.getEntity();
        Cell cell = (Cell) actionImpl.getNextValue();

        FXMLLoader fxmlLoader = getFXMLLoader();
        DefaultHistoryCellController controller = fxmlLoader.getController();
        controller.primaryIcon.setImage(unit.getUnitType().getIconImage());
        controller.secondaryIcon.setImage(getMoveIcon());

        return fxmlLoader.getRoot();
    }

    @Override
    public boolean supports(Action action) {
        if ( !(action instanceof ActionImpl)) {
            return false;
        }
        ActionImpl actionImpl = (ActionImpl) action;
        return actionImpl.getEntity() instanceof Unit
            && actionImpl.getNextValue() instanceof Cell;
    }

    private Image getMoveIcon() {
        return new Image(
                getClass().getResource("/assets/icons/operation/footstepsWhite.png").toExternalForm(),
                40, 40,
                true, true
        );
    }
}
