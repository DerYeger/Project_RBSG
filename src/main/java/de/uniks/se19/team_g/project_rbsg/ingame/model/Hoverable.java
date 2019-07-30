package de.uniks.se19.team_g.project_rbsg.ingame.model;

import javafx.beans.value.ObservableBooleanValue;
import org.springframework.lang.Nullable;

public interface Hoverable {

    boolean isHovered();

    ObservableBooleanValue hoveredProperty();

    void setHoveredIn(@Nullable Game game);
}
