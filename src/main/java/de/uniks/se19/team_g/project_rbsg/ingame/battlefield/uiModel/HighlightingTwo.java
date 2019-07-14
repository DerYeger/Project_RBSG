package de.uniks.se19.team_g.project_rbsg.ingame.uiModel;

import org.springframework.lang.*;

/**
 * @author Georg Siebert
 */

public enum HighlightingTwo
{
    NONE("None"),
    HOVERED("Hovered"),
    SELECTED("Selected");

    private final String highlight;

    HighlightingTwo(@NonNull String highlight)
    {
        this.highlight = highlight;
    }

    @Override
    public String toString()
    {
        return highlight;
    }

}
