package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
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

        final Object entity = modelManager.getEntityById(event.entityId);
        final Object from = modelManager.getEntityById(event.fromId);

        if (entity == null) {
            logger.error("unknown identity {} changed", event.entityId);
            return null;
        }

        if (from == null) {
            logger.error("unknown from {} for entity {}", event.fromId, event.entityId);
            return null;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(from);
        Object fromProperty = beanWrapper.getPropertyValue(event.fieldName);

        if (fromProperty instanceof List) {
            return null;
        } else {
            return new RemoveAction(entity, beanWrapper, event.fieldName);
        }
    }


}
