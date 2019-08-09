package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TileDrawer;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.application.Platform;
import org.springframework.lang.NonNull;

public class ActionExecutor
{

    private final IngameApi api;
    private TileDrawer tileDrawer;

    public ActionExecutor(@NonNull final IngameApi api)
    {
        this.api = api;
    }

    public ActionExecutor setTileDrawer(@NonNull final TileDrawer tileDrawer)
    {
        this.tileDrawer = tileDrawer;
        return this;
    }


    public void execute(@NonNull final MovementAction action)
    {
        final Unit unit = action.unit;
        final Tour tour = action.tour;
        unit.setRemainingMovePoints(unit.getRemainingMovePoints() - tour.getCost());
        if (unit.getRemainingMovePoints() == 0)
        {
            unit.clearSelection();
        }
        api.move(unit, tour);
        unit.getGame().setInitiallyMoved(true);
    }

    public void execute(@NonNull final AttackAction action)
    {
        action.unit.setAttackReady(false)
                .getGame()
                .setSelectedUnit(null);
        api.attack(action.unit, action.target);
        if (tileDrawer != null)
        {
            Platform.runLater(() ->
                    tileDrawer.drawTile(action.target.getPosition().getTile())
            );
        }
    }

    public void execute(@NonNull final PassAction action)
    {
        api.endPhase();
        action.game.clearSelection();
    }
}