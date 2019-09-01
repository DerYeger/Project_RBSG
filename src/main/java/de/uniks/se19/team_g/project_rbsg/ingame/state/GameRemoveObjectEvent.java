package de.uniks.se19.team_g.project_rbsg.ingame.state;

public class GameRemoveObjectEvent extends GameEvent {

    public static final String NAME = "gameRemoveObject";

    public final String entityId;
    public final String fieldName;
    public final String fromId;

    public GameRemoveObjectEvent(String entityId, String fromId, String fieldName) {
        this.entityId = entityId;
        this.fieldName = fieldName;
        this.fromId = fromId;
    }
}
