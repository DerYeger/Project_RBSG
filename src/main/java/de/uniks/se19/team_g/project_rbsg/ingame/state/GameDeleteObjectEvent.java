package de.uniks.se19.team_g.project_rbsg.ingame.state;

public class GameDeleteObjectEvent extends GameEvent {

    public static final String NAME = "gameRemoveObject";

    public final String entityId;
    public final String fieldName;
    public final String fromId;

    public GameDeleteObjectEvent(String entityId, String fieldName, String fromId) {
        this.entityId = entityId;
        this.fieldName = fieldName;
        this.fromId = fromId;
    }
}
