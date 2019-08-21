package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.util.PlayerUtil;
import de.uniks.se19.team_g.project_rbsg.ingame.model.util.UnitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultRemoveListener implements GameEventDispatcher.Listener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher dispatcher) {
        if (!(gameEvent instanceof GameRemoveObjectEvent)) {
            return;
        }

        GameRemoveObjectEvent removeEvent = (GameRemoveObjectEvent) gameEvent;
        ModelManager modelManager = dispatcher.getModelManager();

        Action action = actionFromRemove(removeEvent, modelManager);

        if (action != null) {
            modelManager.addAction(action);
        }
    }

    private Action actionFromRemove(GameRemoveObjectEvent event, ModelManager modelManager) {

        Object entity = modelManager.getEntityById(event.entityId);
        Object from = modelManager.getEntityById(event.fromId);

        if (entity == null) {
            logger.error("unknown identity {} changed", event.entityId);
            return null;
        }

        if (from == null) {
            logger.error("unknown from {} for entity {}", event.fromId, event.entityId);
            return null;
        }

        final String fieldName = event.fieldName;
        String internalFieldName = fieldName;

        BeanWrapper fromWrapper = new BeanWrapperImpl(from);
        Object fromProperty = fromWrapper.getPropertyValue(event.fieldName);

        if (fromProperty instanceof List) {
            // update other side of relationship, if it's one to many, just flip everything
            String mappedFieldName = null;
            if (from instanceof Game) {
                if (Game.UNITS.equals(fieldName)) {
                    mappedFieldName = "game";
                } else if (Game.PLAYERS.equals(fieldName)) {
                    mappedFieldName = "currentGame";
                }
            } else if (from instanceof Player) {
                if (Player.UNITS.equals(fieldName)) {
                    mappedFieldName = "leader";
                }
            }

            if (mappedFieldName == null) {
                logger.error(
                        "can't update to many relation for entity {}: {} of {}",
                        event.entityId,
                        event.fieldName,
                        event.fromId
                );
                return null;
            }

            internalFieldName = mappedFieldName;
            Object o = entity;
            entity = from;
            from = o;
            fromWrapper = new BeanWrapperImpl(from);
        }

        return new RemoveAction(entity, fromWrapper, internalFieldName);
    }
}
