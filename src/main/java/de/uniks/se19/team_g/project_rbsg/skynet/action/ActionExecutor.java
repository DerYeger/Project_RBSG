package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.*;
import de.uniks.se19.team_g.project_rbsg.ingame.event.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.application.*;
import org.slf4j.*;
import org.springframework.lang.*;

import java.util.*;

public class ActionExecutor
{

    private final IngameApi api;
    private TileDrawer tileDrawer;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private BattleFieldController battleFieldController;

    public ActionExecutor (@NonNull final IngameApi api)
    {
        this.api = api;
    }

    public ActionExecutor setTileDrawer (@NonNull final TileDrawer tileDrawer)
    {
        this.tileDrawer = tileDrawer;
        return this;
    }

    public void execute (@NonNull final Action action)
    {
        if (action instanceof FallbackAction)
        {
            executeFallback((FallbackAction) action);
        }
        else if (action instanceof MovementAction)
        {
            executeMove((MovementAction) action);
        }
        else if (action instanceof AttackAction)
        {
            executeAttack((AttackAction) action);
        }
        else if (action instanceof PassAction)
        {
            executePass((PassAction) action);
        }
        else if (action instanceof SurrenderAction)
        {
            executeSurrender();
        }
    }


    private void executeMove (@NonNull final MovementAction action)
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

    private void executeAttack (@NonNull final AttackAction action)
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

    private void executeFallback (@NonNull final FallbackAction fallbackAction)
    {
        execute(fallbackAction.getAction());
        if (fallbackAction.getNextAction().isPresent())
        {
            execute(fallbackAction.getNextAction().get());
        }
    }

    private void executePass (@NonNull final PassAction action)
    {
        api.endPhase();
        action.game.clearSelection();
    }

    private void executeSurrender ()
    {
        logger.debug("Executing Surrender!");
        if(Objects.nonNull(battleFieldController)) {
            battleFieldController.surrender();
        }
        else {
            api.leaveGame();
        }
    }

    public ActionExecutor setBattleFieldController (BattleFieldController battleFieldController)
    {
        this.battleFieldController = battleFieldController;
        return this;
    }
}