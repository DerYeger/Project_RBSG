package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UnitDeathAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class UnitDeathRenderer extends DefaultActionRenderer {

    public UnitDeathRenderer(@Qualifier("fxmlLoader") ObjectFactory<FXMLLoader> loaderFactory) {
        super(loaderFactory);
    }

    @Nonnull
    @Override
    protected HistoryRenderData doRender(Action action) {
        UnitDeathAction actionImpl = (UnitDeathAction) action;

        Unit unit = actionImpl.getUnit();
        Color color = Color.web(actionImpl.getPlayer().getColor());

        FXMLLoader fxmlLoader = getFXMLLoader();
        DefaultHistoryCellController controller = fxmlLoader.getController();
        controller.primaryIcon.setImage(getIcon("/assets/icons/operation/death-skull.png"));
        controller.secondaryIcon.setImage(unit.getUnitType().getIconImage());


        return new HistoryRenderData(fxmlLoader.getRoot(), color);
    }

    @Override
    public boolean supports(Action action) {
        return action instanceof UnitDeathAction;
    }

}
