package de.uniks.se19.team_g.project_rbsg.ingame.uiModel;

import org.springframework.lang.NonNull;

public enum TileHighlighting
{
    NONE("None"), HOVERED("Hovered"), SELECTED("Selected"), MOVE("Move"), ATTACK("Attack");

    private final String highlight;

    TileHighlighting(@NonNull String highlight)
    {
        this.highlight = highlight;
    }

    @Override
    public String toString() {
        return highlight;
    }

}
