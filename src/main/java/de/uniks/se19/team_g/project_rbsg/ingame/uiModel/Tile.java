package de.uniks.se19.team_g.project_rbsg.ingame.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.TileUtils;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Unit;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;


public class Tile
{
    private final Cell cell;
    private final Image backgroundImage;
    private SimpleObjectProperty<TileHighlighting> highlighting;
    private Unit unit;

    public Tile(Cell cell) {
        this.cell = cell;
        highlighting = new SimpleObjectProperty<>(TileHighlighting.NONE);
        backgroundImage = TileUtils.getBackgroundImage(cell);
        unit = null;
    }

    public Unit getUnit()
    {
        return unit;
    }

    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    public SimpleObjectProperty<TileHighlighting> highlightingProperty() {
        return highlighting;
    }

    public TileHighlighting getHighlighting() {
        return highlighting.get();
    }

    public void setHighlighting(TileHighlighting highlighting)
    {
        this.highlighting.set(highlighting);
    }

    public Image getBackgroundImage()
    {
        return backgroundImage;
    }

    public Cell getCell()
    {
        return cell;
    }
}
