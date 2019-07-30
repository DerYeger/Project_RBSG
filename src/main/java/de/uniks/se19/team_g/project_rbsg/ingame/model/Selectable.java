package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.Nullable;

public interface Selectable {

    boolean isSelected();

    void setSelectedIn(@Nullable Game game);

    default void clearSelection() {setSelectedIn(null);}
}
