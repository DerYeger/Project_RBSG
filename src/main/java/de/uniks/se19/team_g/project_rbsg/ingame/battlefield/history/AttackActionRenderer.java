package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UpdateAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;

@Component
public class AttackActionRenderer extends DefaultActionRenderer {
    public AttackActionRenderer(@Qualifier("fxmlLoader") ObjectFactory<FXMLLoader> loaderFactory) {
        super(loaderFactory);
    }

    @Override
    protected URL getFxmlUrl() {
        return getClass().getResource("/ui/ingame/battleField/attackHistoryCell.fxml");
    }

    @Nonnull
    @Override
    protected HistoryRenderData doRender(Action action) {
        UpdateAction actionImpl = (UpdateAction) action;
        Unit unit = (Unit) actionImpl.getEntity();
        int hp = (int) actionImpl.getNextValue();

        Pair<AttackHistoryCellController, HBox> data = loadCell();

        Color playerColor;
        if (unit.getLeader() != null) {
            playerColor = Color.web(unit.getLeader().getColor());
        } else {
            playerColor = null;
        }


        AttackHistoryCellController controller = data.getKey();
        controller.primaryIcon.setImage(getAttackIcon());
        controller.secondaryIcon.setImage(unit.getUnitType().getIconImage());
        controller.setHealth(hp);

        return new HistoryRenderData(data.getValue(), playerColor);
    }

    @Override
    public boolean supports(Action action) {
        if ( !(action instanceof UpdateAction)) {
            return false;
        }
        UpdateAction actionImpl = (UpdateAction) action;
        return actionImpl.getEntity() instanceof Unit
                && "hp".equals(actionImpl.getFieldName());
    }

    private Image getAttackIcon() {
        return new Image(
                getClass().getResource("/assets/icons/operation/swordClashWhite.png").toExternalForm(),
                40, 40,
                true, true
        );
    }
}
