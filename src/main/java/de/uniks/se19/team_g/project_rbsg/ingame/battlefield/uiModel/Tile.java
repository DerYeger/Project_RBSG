package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.image.*;
import org.springframework.lang.Nullable;

import java.beans.*;

/**
 * @author Georg Siebert
 */

public class Tile
{
    private final Cell cell;
    private final Image backgroundImage;
    private final Image deckoratorImage;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final InvalidationListener updateHighlightingOne = this::updateHighlightingOne;
    private HighlightingOne highlightingOne;
    private HighlightingTwo highlightingTwo;

    public Tile(Cell cell)
    {
        cell.setTile(this);
        this.cell = cell;
        highlightingOne = HighlightingOne.NONE;
        highlightingTwo = HighlightingTwo.NONE;

        backgroundImage = TileUtils.getBackgroundImage(cell);

        if (cell.getBiome() == Biome.GRASS)
        {
            deckoratorImage = TileUtils.getDecoratorImage();
        }
        else
        {
            deckoratorImage = null;
        }

        configureUnitDependencies();

        configureHighlighting();
    }

    private void configureUnitDependencies() {
        cell.unitProperty().addListener((observable, lastUnit, nextUnit) -> {
            clearUnitListener(lastUnit);
            addUnitListener(nextUnit);
        });
    }

    private void clearUnitListener(@Nullable Unit unit) {
        if (unit == null) {
            return;
        }

        unit.attackableProperty().removeListener(updateHighlightingOne);
    }

    private void addUnitListener(@Nullable Unit unit) {
        if (unit == null) {
            return;
        }

        unit.attackableProperty().addListener(updateHighlightingOne);
    }

    private void configureHighlighting() {
        cell.isReachableProperty().addListener(updateHighlightingOne);
        cell.isAttackableProperty().addListener(updateHighlightingOne);
    }

    private HighlightingOne evaluateHighlightingOne() {
        if (cell.getUnit() != null && cell.getUnit().isSelected()) {
            return HighlightingOne.NONE;
        }

        if (cell.isIsAttackable()) {
            return HighlightingOne.ATTACK;
        }

        if (cell.isIsReachable()) {
            return HighlightingOne.MOVE;
        }

        return HighlightingOne.NONE;
    }

    private void evaluateHightlightingTwo() {

    }

    public void removeListener(PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }

    public void addListener(PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    public Image getBackgroundImage()
    {
        return backgroundImage;
    }

    public HighlightingTwo getHighlightingTwo()
    {
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

    private void updateHighlightingOne(@SuppressWarnings("unused") Observable observable)
    {
        this.highlightingOne = evaluateHighlightingOne();

        //Abusing property changed

        pcs.firePropertyChange("HighlightingOne", this, this.highlightingOne);
    }

    public Image getDeckoratorImage()
    {
        return deckoratorImage;
    }
}
