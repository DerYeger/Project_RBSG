package de.uniks.se19.team_g.project_rbsg.ingame.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.TileUtils;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Unit;
import javafx.scene.image.Image;


public class Tile
{
    private Cell cell;
    private Image backgroundImage;
    private TileHighlighting highlighting;
    private Unit unit;

    public Tile(Cell cell, TileHighlighting highlighting) {
        this.cell = cell;
        this.highlighting = highlighting;
        backgroundImage = TileUtils.getBackgroundImage(cell);
        unit = null;
    }

    public TileHighlighting getHighlighting()
    {
        return highlighting;
    }

    public void setHighlighting(TileHighlighting highlighting)
    {
        this.highlighting = highlighting;
    }

    public Unit getUnit()
    {
        return unit;
    }

    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }
}
