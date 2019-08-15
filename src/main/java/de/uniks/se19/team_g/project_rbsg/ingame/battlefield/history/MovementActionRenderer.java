package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UpdateAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class MovementActionRenderer extends DefaultActionRenderer {

    public MovementActionRenderer(
            @Qualifier("fxmlLoader") ObjectFactory<FXMLLoader> loaderFactory
    ) {
        super(loaderFactory);
    }

    @Override
    @Nonnull
    protected HistoryRenderData doRender(Action action) {
        UpdateAction actionImpl = (UpdateAction) action;
        Unit unit = (Unit) actionImpl.getEntity();
        Cell cell = (Cell) actionImpl.getNextValue();

        FXMLLoader fxmlLoader = getFXMLLoader();
        DefaultHistoryCellController controller = fxmlLoader.getController();
        controller.primaryIcon.setImage(unit.getUnitType().getIconImage());
        controller.secondaryIcon.setImage(getMoveIcon());

        Node root = fxmlLoader.getRoot();

        return new HistoryRenderData(root, Color.web(unit.getLeader().getColor()));
    }

    @Override
    public boolean supports(Action action) {
        if ( !(action instanceof UpdateAction)) {
            return false;
        }
        UpdateAction actionImpl = (UpdateAction) action;
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
