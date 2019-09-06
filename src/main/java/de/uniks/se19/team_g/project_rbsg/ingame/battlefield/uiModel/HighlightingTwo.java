package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import org.springframework.lang.*;

/**
 * @author Georg Siebert
 */

public enum HighlightingTwo
{
    NONE("None"),
    HOVERED("Hovered"),
    SELECTED("Selected"),
    SELECETD_WITH_UNITS("Selected_with_Units");

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
