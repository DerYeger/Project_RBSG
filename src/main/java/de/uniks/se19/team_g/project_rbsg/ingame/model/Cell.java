package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class Cell {

    @NonNull
    private final String id;

    private Game game;

    private Biome biome;

    private boolean isPassable;

    private int x;
    private int y;

    private Cell left;
    private Cell top;
    private Cell right;
    private Cell bottom;

    private Unit unit;

    public Cell(@NonNull final String id) {
        this.id = id;
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

    public Cell getLeft() {
        return left;
    }

    public Cell getTop() {
        return top;
    }

    public Cell getRight() {
        return right;
    }

    public Cell getBottom() {
        return bottom;
    }

    public Unit getUnit() {
        return unit;
    }

    public Cell setGame(@NonNull final Game game) {
        doSetGame(game);
        game.withCell(this);
        return this;
    }

    Cell doSetGame(@NonNull final Game game) {
        this.game = game;
        return this;
    }

    public Cell setBiome(@NonNull final Biome biome) {
        this.biome = biome;
        return this;
    }

    public Cell setPassable(@NonNull final boolean passable) {
        isPassable = passable;
        return this;
    }

    public Cell setX(@NonNull final int x) {
        this.x = x;
        return this;
    }

    public Cell setY(@NonNull final int y) {
        this.y = y;
        return this;
    }

    public Cell setLeft(@Nullable final Cell left) {
        doSetLeft(left);
        if (left != null) left.doSetRight(this);
        return this;
    }

    private void doSetLeft(@Nullable final Cell left) {
        this.left = left;
    }

    public Cell setTop(@Nullable final Cell top) {
        doSetTop(top);
        if (top != null) top.doSetBottom(this);
        return this;
    }

    private void doSetTop(@Nullable final Cell top) {
        this.top = top;
    }

    public Cell setRight(@Nullable final Cell right) {
        doSetRight(right);
        if (right != null) right.doSetLeft(this);
        return this;
    }

    private void doSetRight(@Nullable final Cell right) {
        this.right = right;
    }

    public Cell setBottom(@Nullable final Cell bottom) {
        doSetBottom(bottom);
        if (bottom != null) bottom.doSetTop(this);
        return this;
    }

    private void doSetBottom(@Nullable final Cell bottom) {
        this.bottom = bottom;
    }

    public Cell setUnit(@Nullable final Unit unit) {
        if (this.unit != null) this.unit.setPosition(null);
        doSetUnit(unit);
        if (unit != null) unit.doSetPosition(this);
        return this;
    }

    void doSetUnit(@Nullable final Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "(" + biome + ", " + x + ", " + y + ")";
    }
}
