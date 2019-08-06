package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TileDrawer;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import org.springframework.lang.NonNull;

public class ActionExecutor {

    private final IngameApi api;
    private TileDrawer tileDrawer;

    public ActionExecutor(@NonNull final IngameApi api) {
        this.api = api;
    }

    public ActionExecutor setTileDrawer(@NonNull final TileDrawer tileDrawer) {
        this.tileDrawer = tileDrawer;
        return this;
    }


    public void execute(@NonNull final MovementAction action) {
        api.move(action.unit, action.tour);
    }

    public void execute(@NonNull final AttackAction action) {
        api.attack(action.unit, action.target);
        if (tileDrawer != null) {
            tileDrawer.drawTile(action.target.getPosition().getTile());
        }
    }
}