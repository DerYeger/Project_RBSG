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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final IngameApi api;
    private TileDrawer tileDrawer;
    private Runnable surrenderGameAction;

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
        logger.debug("Reduced MP of " + unit + " by " + tour.getCost() + " to " + unit.getRemainingMovePoints());
        if (unit.getRemainingMovePoints() == 0)
        {
            unit.clearSelection();
            logger.debug("Unselected " + unit);
        }
        api.move(unit, tour);
        unit.getGame().setInitiallyMoved(true);
        logger.info("Moved " + unit + " to " + tour.getTarget());
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
        logger.info("Attacked " + action.target + " with " + action.unit);
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
        logger.info("Passed");
    }

    private void executeSurrender ()
    {
        if(Objects.nonNull(surrenderGameAction)) {
            surrenderGameAction.run();
        }
        else {
            api.leaveGame();
        }
        logger.info("Surrendered");
    }

    public ActionExecutor setSurrenderGameAction (Runnable surrenderGameAction)
    {
        this.surrenderGameAction = surrenderGameAction;
        return this;
    }
}