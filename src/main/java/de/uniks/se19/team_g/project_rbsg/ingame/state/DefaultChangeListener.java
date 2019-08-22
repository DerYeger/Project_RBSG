package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class DefaultChangeListener implements GameEventDispatcher.Listener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher dispatcher) {
        if (!(gameEvent instanceof GameChangeObjectEvent)) {
            return;
        }

        GameChangeObjectEvent changeEvent = (GameChangeObjectEvent) gameEvent;
        ModelManager modelManager = dispatcher.getModelManager();

        Action action = actionFromChange(changeEvent, modelManager);

        if (action != null) {
            modelManager.addAction(action);
        }

    }

    @Nullable
    private Action actionFromChange(GameChangeObjectEvent event, ModelManager modelManager) {

        final Object entity = modelManager.getEntityById(event.getEntityId());

        if (entity == null) {
            logger.error("unknown identity {} changed", event.getEntityId());
            return null;
        }

        Object newValue = modelManager.getEntityById(event.getNewValue());

        if (newValue == null) {
            newValue = event.getNewValue();
        }

        try {
            return new UpdateAction(event.getFieldName(), newValue, entity);
        } catch (BeansException e) {
            logger.error("can't derive entity update from change event", e);
        }

        logger.error("can't update entity of type {}", entity.getClass());

        return null;

    }
}
