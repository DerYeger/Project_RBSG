package de.uniks.se19.team_g.project_rbsg.ingame.state;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class UpdateAction implements Action {

    protected final String fieldName;
    protected final Object nextValue;
    protected final BeanWrapperImpl beanWrapper;

    protected Object previousValue;


    public UpdateAction(String fieldName, Object nextValue, Object entity) throws BeansException {

        beanWrapper = new BeanWrapperImpl(entity);
        this.nextValue = beanWrapper.convertForProperty(nextValue, fieldName);
        this.fieldName = fieldName;

    }


    @Override
    public void run() {
        if (previousValue == null) {
            previousValue = beanWrapper.getPropertyValue(fieldName);
        }

        beanWrapper.setPropertyValue(fieldName, nextValue);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getNextValue() {
        return nextValue;
    }

    public Object getEntity() {
        return beanWrapper.getWrappedInstance();
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    @Override
    public void undo() {
        // hacky as fuck
        if (previousValue != null) {
            beanWrapper.setPropertyValue(fieldName, previousValue);
        }
    }
}
