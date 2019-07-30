package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.Nullable;

public interface Hoverable {

    boolean isHovered();

    void setHoveredIn(@Nullable Game game);
}
