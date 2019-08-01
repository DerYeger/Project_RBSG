package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TileUtils;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Georg Siebert
 */

public class Tile
{
    private final Cell cell;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final ChangeListener<Object> updateHighlightingOne = this::updateHighlightingOne;

    private final ChangeListener<Object> updateHighlightingTwo = this::updateHighlightingTwo;

    private Image backgroundImage;
    private Image deckoratorImage;

    private HighlightingOne highlightingOne;
    private HighlightingTwo highlightingTwo;

    public Tile(Cell cell)
    {
        cell.setTile(this);
        this.cell = cell;
        highlightingOne = HighlightingOne.NONE;
        highlightingTwo = HighlightingTwo.NONE;

        configureUnitDependencies();
        configureCellDependencies();
        configureGameDependencies();

        updateHighlightingOne(null, null, null);
        updateHighlightingTwo(null, null, null);

    }

    private void configureGameDependencies() {
        cell.getGame().selectedProperty().addListener(updateHighlightingOne);
        cell.getGame().phaseProperty().addListener(updateHighlightingOne);
    }

    private void configureUnitDependencies() {
        cell.unitProperty().addListener((observable, lastUnit, nextUnit) -> {

            clearUnitListener(lastUnit);
            addUnitListener(nextUnit);

            updateHighlightingOne(observable, null, null);
            updateHighlightingTwo(observable, null, null);
        });

        // init
        addUnitListener(cell.getUnit());
    }

    private void clearUnitListener(@Nullable Unit unit) {
        if (unit == null) {
            return;
        }

        unit.attackableProperty().removeListener(updateHighlightingOne);
        unit.selectedProperty().removeListener(updateHighlightingTwo);
        unit.hoveredProperty().removeListener(updateHighlightingTwo);
    }

    private void addUnitListener(@Nullable Unit unit) {
        if (unit == null) {
            return;
        }

        unit.attackableProperty().addListener(updateHighlightingOne);
        unit.selectedProperty().addListener(updateHighlightingTwo);
        unit.hoveredProperty().addListener(updateHighlightingTwo);
    }

    private void configureCellDependencies() {
        cell.isReachableProperty().addListener(updateHighlightingOne);
        cell.isAttackableProperty().addListener(updateHighlightingOne);
        cell.hoveredProperty().addListener(updateHighlightingTwo);
        cell.selectedProperty().addListener(updateHighlightingOne);

        if (cell.getTop() != null) {
            cell.getTop().unitProperty().addListener(updateHighlightingOne);
        }
        if (cell.getLeft() != null) {
            cell.getLeft().unitProperty().addListener(updateHighlightingOne);
        }
        if (cell.getBottom() != null) {
            cell.getBottom().unitProperty().addListener(updateHighlightingOne);
        }
        if (cell.getRight() != null) {
            cell.getRight().unitProperty().addListener(updateHighlightingOne);
        }
    }

    private HighlightingOne evaluateHighlightingOne() {

        Unit unit = cell.getUnit();

        if (unit != null && unit.isSelected()) {
            return HighlightingOne.NONE;
        }

        HighlightingOne highlighting = getAttackHighlighting();
        if (highlighting != null) {
            return highlighting;
        }

        if (cell.isIsReachable()) {
            return HighlightingOne.MOVE;
        }

        return HighlightingOne.NONE;
    }

    @Nullable
    private HighlightingOne getAttackHighlighting() {
        Unit unit = cell.getUnit();
        Unit selectedUnit = cell.getGame().getSelectedUnit();

        if (selectedUnit == null) {
            return null;
        }

        if (
            cell.getGame().isPhase(Game.Phase.attackPhase)
            && cell.getGame().getCurrentPlayer().isPlayer()
        ) {
            // highlight neighbor fields of selected unit
            if (!cell.isNeighbor(selectedUnit.getPosition())) {
                return null;
            }
            return selectedUnit.canAttack(unit) ?
                HighlightingOne.ATTACK
                : HighlightingOne.ATTACK_BLOCKED
            ;
        }

        return null;
    }

    private HighlightingTwo evaluateHightlightingTwo() {

        Unit unit = cell.getUnit();

        if ( unit != null) {
            if (unit.isSelected()) {
                return  unit.getLeader().isPlayer() ?
                    HighlightingTwo.SELECETD_WITH_UNITS
                    : HighlightingTwo.SELECTED;
            }
            if (unit.isHovered()) {
                return HighlightingTwo.HOVERED;
            }
        }

        if (cell.isSelected()) {
            return HighlightingTwo.SELECTED;
        }

        if (cell.isHovered()) {
            return HighlightingTwo.HOVERED;
        }

        return HighlightingTwo.NONE;
    }

    public void removeListener(PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }

    public void addListener(PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    public HighlightingTwo getHighlightingTwo()
    {
        return highlightingTwo;
    }

    @SuppressWarnings("unused")
    private void updateHighlightingTwo(Observable observable, Object prev, Object next)
    {
        HighlightingTwo oldValue = highlightingTwo;
        HighlightingTwo nextValue = evaluateHightlightingTwo();

        if (oldValue != nextValue) {
            this.highlightingTwo = nextValue;
            //Abusing property changed
            pcs.firePropertyChange("HighlightingTwo", this, this.highlightingTwo);
        }
    }

    public Cell getCell()
    {
        return cell;
    }

    public HighlightingOne getHighlightingOne()
    {
        return highlightingOne;
    }

    @SuppressWarnings("unused")
    private void updateHighlightingOne(Observable observable, Object prev, Object next)
    {
        HighlightingOne oldVal = this.highlightingOne;
        HighlightingOne nextVal = evaluateHighlightingOne();

        if (oldVal != nextVal) {
            this.highlightingOne = nextVal;
            //Abusing property changed
            pcs.firePropertyChange("HighlightingOne", this, this.highlightingOne);
        }
    }

    public Image getDeckoratorImage()
    {

        if (cell.getBiome() == Biome.GRASS && deckoratorImage == null) {
            deckoratorImage = TileUtils.getDecoratorImage();
        }
        return deckoratorImage;
    }

    public Image getBackgroundImage()
    {
        if (backgroundImage == null) {
            backgroundImage = TileUtils.getBackgroundImage(cell);
        }
        return backgroundImage;
    }
}
