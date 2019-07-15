package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import org.springframework.lang.*;

/**
 * @author Georg Siebert
 */
public enum HighlightingOne
{
    NONE("None"),
    ATTACK("Attack"),
    MOVE("Move"),
    BORDER("Border");

    private final String highlight;

    HighlightingOne(@NonNull String highlight)
    {
        this.highlight = highlight;
    }

    @Override
    public String toString()
    {
        return highlight;
    }
}
