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
    private final Image deckoratorImage;
    private HighlightingOne highlightingOne;
    private HighlightingTwo highlightingTwo;

    public Tile(Cell cell) {
        this.cell = cell;
        highlightingOne = HighlightingOne.NONE;
        highlightingTwo = HighlightingTwo.NONE;

        backgroundImage = TileUtils.getBackgroundImage(cell);
        //TODO: Add decorator to Tile Utils
        deckoratorImage = null;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public HighlightingTwo getHighlightingTwo() {
        return highlightingTwo;
    }

    public void setHighlightingTwo(HighlightingTwo highlightingTwo)
    {
        this.highlightingTwo = highlightingTwo;
    }

    public Cell getCell()
    {
        return cell;
    }

    public HighlightingOne getHighlightingOne()
    {
        return highlightingOne;
    }

    public void setHighlightingOne(HighlightingOne highlightingOne)
    {
        this.highlightingOne = highlightingOne;
    }

    public Image getDeckoratorImage()
    {
        return deckoratorImage;
    }
}
