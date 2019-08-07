package de.uniks.se19.team_g.project_rbsg.ingame.model;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jan Müller
 */
public class Cell implements Hoverable, Selectable {

    @Nonnull
    private final String id;

    private Tile tile;

    private Game game;

    private Biome biome;

    private boolean isPassable;

    private int x;
    private int y;

    private Cell left;
    private Cell top;
    private Cell right;
    private Cell bottom;

    private final SimpleObjectProperty<Unit> unit;

    private final SimpleBooleanProperty isReachable = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty isAttackable = new SimpleBooleanProperty(false);

    private final ObjectProperty<Game> selectedIn = new SimpleObjectProperty<>();
    private final BooleanBinding isSelected = selectedIn.isNotNull();

    private final ObjectProperty<Game> hoveredIn = new SimpleObjectProperty<>();
    private final BooleanBinding isHovered = hoveredIn.isNotNull();

    public Cell(@Nonnull final String id) {
        this.id = id;

        unit = new SimpleObjectProperty<>();
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Biome getBiome() {
        return biome;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Nullable
    public Cell getLeft() {
        return left;
    }

    @Nullable
    public Cell getTopLeft() {
        if (top == null || left == null) return null;
        return top.getLeft();
    }

    @Nullable
    public Cell getTop() {
        return top;
    }

    @Nullable
    public Cell getTopRight() {
        if (top == null || right == null) return null;
        return top.getRight();
    }

    @Nullable
    public Cell getRight() {
        return right;
    }

    @Nullable
    public Cell getBottomRight() {
        if (bottom == null || right == null) return null;
        return bottom.getRight();
    }

    @Nullable
    public Cell getBottom() {
        return bottom;
    }

    @Nullable
    public Cell getBottomLeft() {
        if (bottom == null || left == null) return null;
        return bottom.getLeft();
    }

    public ReadOnlyObjectProperty<Unit> unitProperty() {
        return unit;
    }

    @Nullable
    public Unit getUnit() {
        return unit.get();
    }

    public Cell setGame(@Nullable final Game game) {
        if (this.game == game) return this;
        if (this.game != null) this.game.doRemoveCell(this);
        doSetGame(game);
        if (game != null) game.doAddCell(this);
        return this;
    }

    Cell doSetGame(@Nullable final Game game) {
        this.game = game;
        return this;
    }

    public Cell setBiome(@Nonnull final Biome biome) {
        this.biome = biome;
        return this;
    }

    public Cell setPassable(final boolean passable) {
        isPassable = passable;
        return this;
    }

    public Cell setX(final int x) {
        this.x = x;
        return this;
    }

    public Cell setY(final int y) {
        this.y = y;
        return this;
    }

    public Cell setLeft(@Nullable final Cell left) {
        if (this.left == left) return this;
        if (this.left != null) this.left.doSetRight(null);
        doSetLeft(left);
        if (left != null) left.doSetRight(this);
        return this;
    }

    private void doSetLeft(@Nullable final Cell left) {
        this.left = left;
    }

    public Cell setTop(@Nullable final Cell top) {
        if (this.top == top) return this;
        if (this.top != null) this.top.doSetBottom(null);
        doSetTop(top);
        if (top != null) top.doSetBottom(this);
        return this;
    }

    private void doSetTop(@Nullable final Cell top) {
        this.top = top;
    }

    public Cell setRight(@Nullable final Cell right) {
        if (this.right == right) return this;
        if (this.right != null) this.right.doSetLeft(null);
        doSetRight(right);
        if (right != null) right.doSetLeft(this);
        return this;
    }

    private void doSetRight(@Nullable final Cell right) {
        this.right = right;
    }

    public Cell setBottom(@Nullable final Cell bottom) {
        if (this.bottom == bottom) return this;
        if (this.bottom != null) this.bottom.doSetTop(null);
        doSetBottom(bottom);
        if (bottom != null) bottom.doSetTop(this);
        return this;
    }

    private void doSetBottom(@Nullable final Cell bottom) {
        this.bottom = bottom;
    }

    public Cell setUnit(@Nullable final Unit unit) {
        if (this.unit.get() == unit) return this;
        if (this.unit.get() != null) this.unit.get().doSetPosition(null);
        doSetUnit(unit);
        if (unit != null) unit.doSetPosition(this);
        return this;
    }

    void doSetUnit(@Nullable final Unit unit) {
        this.unit.set(unit);
    }

    public void remove() {
        setGame(null);
        setLeft(null);
        setTop(null);
        setRight(null);
        setBottom(null);
        setUnit(null);
    }


    public boolean isIsReachable() {
        return isReachable.get();
    }

    public SimpleBooleanProperty isReachableProperty() {
        return isReachable;
    }

    public void setIsReachable(boolean isReachable) {
        this.isReachable.set(isReachable);
    }

    @Override
    public String toString() {
        return "(" + biome + ", " + x + ", " + y + ")";
    }


    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public boolean isHovered() {
        return isHovered.get();
    }

    @Override
    public ObservableBooleanValue hoveredProperty() {
        return isHovered;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setHoveredIn(@Nullable Game game) {
        Game lastHoveredIn = hoveredIn.get();

        if (game == lastHoveredIn) {
            return;
        }

        hoveredIn.set(game);

        if (lastHoveredIn != null) {
            lastHoveredIn.setHovered(null);
        }

        if (game != null) {
            game.setHovered(this);
        }
    }

    public boolean isSelected() {
        return isSelected.get();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setSelectedIn(@Nullable Game game) {
        // note that this should be just a toggle between null and a game, not between two games
        Game lastState = selectedIn.get();

        if (lastState == game) {
            return;
        }

        selectedIn.set(game);

        if (lastState != null) {
            lastState.setSelected(null);
        }
        if (game != null) {
            game.setSelected(this);
        }
    }

    @Override
    public ObservableBooleanValue selectedProperty() {
        return isSelected;
    }

    public ObjectProperty<Game> selectedInProp() {
        return selectedIn;
    }

    public boolean isNeighbor(Cell other) {
        return other == getRight()
            || other == getBottom()
            || other == getLeft()
            || other == getTop()
        ;
    }

    public ArrayList<Cell> getNeighbors() {
        return Stream
                .of(getRight(), getBottom(), getLeft(), getTop())
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
