package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameRootController;

import javax.annotation.Nonnull;

public interface IngameViewController {

    void configure(@Nonnull IngameContext context, @Nonnull IngameRootController rootController);
}
