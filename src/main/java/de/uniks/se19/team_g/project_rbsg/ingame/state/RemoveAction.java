package de.uniks.se19.team_g.project_rbsg.ingame.state;

import org.springframework.beans.BeanWrapper;

public class RemoveAction implements Action {

    private final Object removedEntity;
    private final BeanWrapper fromWrapper;
    private final String fromField;

    public RemoveAction(Object removedEntity, BeanWrapper fromWrapper, String fromField) {
        this.removedEntity = removedEntity;
        this.fromWrapper = fromWrapper;
        this.fromField = fromField;
    }

    @Override
    public void undo() {
        fromWrapper.setPropertyValue(fromField, removedEntity);
    }

    @Override
    public void run() {
        fromWrapper.setPropertyValue(fromField, null);

    }
}
