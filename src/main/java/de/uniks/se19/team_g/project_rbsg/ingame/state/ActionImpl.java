package de.uniks.se19.team_g.project_rbsg.ingame.state;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class ActionImpl implements Action {

    private final String fieldName;
    private final Object nextValue;
    private final BeanWrapperImpl beanWrapper;

    private Object previousValue;


    public ActionImpl(String fieldName, Object nextValue, Object entity) throws BeansException {

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
}
