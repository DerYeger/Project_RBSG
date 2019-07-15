package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TileUtils;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.scene.image.Image;

import java.beans.*;

/**
 * @author Georg Siebert
 */

public class Tile
{
    private final Cell cell;
    private final Image backgroundImage;
    private final Image deckoratorImage;
    private HighlightingOne highlightingOne;
    private HighlightingTwo highlightingTwo;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void removeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

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

        //Abusing property changed
        pcs.firePropertyChange("HighlightingTwo", this, this.highlightingTwo);
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

        //Abusing property changed

        pcs.firePropertyChange("HighlightingOne", this, this.highlightingOne);
    }

    public Image getDeckoratorImage()
    {
        return deckoratorImage;
    }
}
