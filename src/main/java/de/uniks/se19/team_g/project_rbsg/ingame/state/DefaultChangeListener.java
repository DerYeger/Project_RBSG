package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

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

        modelManager.getExecutor().execute(() -> applyChange(changeEvent, modelManager));

    }

    private void applyChange(GameChangeObjectEvent changeEvent, ModelManager modelManager) {

        final Object entity = modelManager.getEntityById(changeEvent.getEntityId());

        if (entity == null) {
            logger.error("unknown identity {} changed", changeEvent.getEntityId());
            return;
        }

        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(entity);

        Object newValue = modelManager.getEntityById(changeEvent.getNewValue());

        if (newValue == null) {
            newValue = changeEvent.getNewValue();
        }

        try {
            beanWrapper.setPropertyValue(changeEvent.getFieldName(), newValue);
            return;
        } catch (BeansException e) {
            logger.error("entity update failed", e);
        }

        logger.error("can't update entity of type {}", entity.getClass());

    }
}
