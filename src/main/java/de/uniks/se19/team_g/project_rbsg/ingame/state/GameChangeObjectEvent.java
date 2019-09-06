package de.uniks.se19.team_g.project_rbsg.ingame.state;

public class GameChangeObjectEvent extends GameEvent {

    public static final String NAME = "gameChangeObject";

    private final String entityId;

    private final String fieldName;
    private final String newValue;

    public GameChangeObjectEvent(String entityId, String fieldName, String newValue) {

        this.entityId = entityId;
        this.fieldName = fieldName;
        this.newValue = newValue;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getNewValue() {
        return newValue;
    }
}
