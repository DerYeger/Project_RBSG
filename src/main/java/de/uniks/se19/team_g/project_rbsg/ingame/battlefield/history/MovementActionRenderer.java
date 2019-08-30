package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UpdateAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

        FXMLLoader fxmlLoader = getFXMLLoader();
        DefaultHistoryCellController controller = fxmlLoader.getController();
        controller.primaryIcon.setImage(getIcon("/assets/icons/operation/footstepsWhite.png"));
        controller.secondaryIcon.setImage(unit.getUnitType().getPreview());

        Node root = fxmlLoader.getRoot();

        Color color = unit.getLeader() != null ? Color.web(unit.getLeader().getColor()) : null;

        return new HistoryRenderData(root, color);
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

}
