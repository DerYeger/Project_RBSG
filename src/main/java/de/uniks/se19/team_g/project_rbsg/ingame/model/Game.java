package de.uniks.se19.team_g.project_rbsg.ingame.model;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Jan Müller
 */
public class Game {

    private String id;

    private StringProperty phase = new SimpleStringProperty();

    final private SimpleBooleanProperty alreadyMovedThisRound = new SimpleBooleanProperty(false);

    final private SimpleBooleanProperty gameStarted = new SimpleBooleanProperty(false);

    public static final String PLAYERS = "players";
    private ObservableList<Player> players = FXCollections.observableArrayList();

    public static final String UNITS = "units";
    private ObservableList<Unit> units = FXCollections.observableArrayList();

    public static final String CELLS = "cells";
    private ObservableList<Cell> cells = FXCollections.observableArrayList();

    final private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();

    final private ObjectProperty<Unit> selectedUnit = new SimpleObjectProperty<>();

    final private ObjectProperty<Player> winner = new SimpleObjectProperty<>();

    final private ObjectProperty<Selectable> selected = new SimpleObjectProperty<>();

    final private ObjectProperty<Hoverable> hovered = new SimpleObjectProperty<>();

    public Game(@NonNull final String id) {
        this.id = id;

    }

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ObservableList<Player> getPlayers() {
        return players;
    }

    public ObservableList<Unit> getUnits() {
        return units;
    }

    public ObservableList<Cell> getCells() {
        return cells;
    }

    public Game withPlayers(@Nullable final Player ...players) {
        if (players == null) return this;
        return withPlayers(Arrays.asList(players));
    }

    public Game withPlayers(@Nullable final Collection<Player> players)
    {
        if (players != null) {
            for (final Player player : players) {
                if (player != null) withPlayer(player);
            }
        }
        return this;
    }

    public Game withPlayer(@Nullable final Player player) {
        if (players.contains(player)) return this;
        doAddPlayer(player);
        if (player != null) player.doSetGame(this);
        return this;
    }

    void doAddPlayer(@Nullable final Player player) {
        players.add(player);
    }

    public Game withoutPlayers(@Nullable final Player ...players) {
        if (players == null) return this;
        return withoutPlayers(Arrays.asList(players));
    }

    public Game withoutPlayers(@Nullable final Collection<Player> players) {
        if (players != null) {
            for (final Player player : players) {
                if (player != null) withoutPlayer(player);
            }
        }
        return this;
    }

    public Game withoutPlayer(@NonNull final Player player) {
        if (!players.contains(player)) return this;
        doRemovePlayer(player);
        player.doSetGame(null);
        return this;
    }

    void doRemovePlayer(@NonNull final Player player) {
        players.remove(player);
    }

    public Game withUnits(@Nullable final Unit ...units) {
        if (units == null) return this;
        return withUnits(Arrays.asList(units));
    }

    public Game withUnits(@Nullable final Collection<Unit> units) {
        if (units != null) {
            for (final Unit unit : units) {
                if (unit != null) withUnit(unit);
            }
        }
        return this;
    }

    public Game withUnit(@NonNull final Unit unit) {
        if (units.contains(unit)) return this;
        doAddUnit(unit);
        unit.doSetGame(this);
        return this;
    }

    void doAddUnit(@NonNull final Unit unit) {
        units.add(unit);
    }

    public Game withoutUnits(@Nullable final Unit ...units) {
        if (units == null) return this;
        return withoutUnits(Arrays.asList(units));
    }

    public Game withoutUnits(@Nullable final Collection<Unit> units) {
        if (units != null) {
            for (final Unit unit : units) {
                if (unit != null) withoutUnit(unit);
            }
        }
        return this;
    }

    public Game withoutUnit(@NonNull final Unit unit) {
        if (!units.contains(unit)) return this;
        doRemoveUnit(unit);
        unit.doSetGame(null);
        return this;
    }

    void doRemoveUnit(@NonNull final Unit unit) {
        units.remove(unit);
    }

    public Game withCells(@Nullable final Cell ...cells) {
        if (cells == null) return this;
        return withCells(Arrays.asList(cells));
    }

    public Game withCells(@Nullable final Collection<Cell> cells) {
        if (cells != null) {
            for (final Cell cell : cells) {
                if (cell != null) withCell(cell);
            }
        }
        return this;
    }

    public Game withCell(@NonNull final Cell cell) {
        if (cells.contains(cell)) return this;
        doAddCell(cell);
        cell.doSetGame(this);
        return this;
    }

    void doAddCell(@NonNull final Cell cell) {
        cells.add(cell);
    }

    public Game withoutCells(@Nullable final Cell ...cells) {
        if (cells == null) return this;
        return withoutCells(Arrays.asList(cells));
    }

    public Game withoutCells(@Nullable final Collection<Cell> cells) {
        if (cells != null) {
            for (final Cell cell : cells) {
                if (cell != null) withoutCell(cell);
            }
        }
        return this;
    }

    public Game withoutCell(@NonNull final Cell cell) {
        if (!cells.contains(cell)) return this;
        doRemoveCell(cell);
        cell.doSetGame(null);
        return this;
    }

    void doRemoveCell(@NonNull final Cell cell) {
        cells.remove(cell);
    }

    public void remove() {
        withoutPlayers(new ArrayList<>(players));
        withoutUnits(new ArrayList<>(units));
        withoutCells(new ArrayList<>(cells));
    }

    @Override
    public String toString() {
        return "(" + id + ", " + players.size() + " players, " + units.size() + " units)";
    }

    public String getPhase() {
        return phase.get();
    }

    public ReadOnlyStringProperty phaseProperty() {
        return phase;
    }

    public Game setPhase(String phase) {
        this.phase.set(phase);
        return this;
    }

    public Game setCurrentPlayer(Player player) {
        currentPlayer.set(player);
        return this;
    }

    public Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    public ObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer;
    }

    public Player getWinner() {
        return winner.get();
    }

    public ObjectProperty<Player> winnerProperty() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner.set(winner);
    }

    public boolean getInitiallyMoved(){ return alreadyMovedThisRound.get(); }

    public SimpleBooleanProperty initiallyMovedProperty(){ return alreadyMovedThisRound; }

    public Game setInitiallyMoved(boolean initiallyMoved){
        this.alreadyMovedThisRound.set(initiallyMoved);
        return this;
    }


    public Unit getSelectedUnit() {
        Selectable selected = getSelected();
        if (selected instanceof Unit) {
            return (Unit) selected;
        } else {
            return null;
        }
    }

    @Nonnull
    public ObservableObjectValue<Unit> selectedUnitProperty() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        setSelected(selectedUnit);
    }

    public Selectable getSelected() {
        return selected.get();
    }

    public ObservableObjectValue<Selectable> selectedProperty() {
        return selected;
    }

    // use selecteable .setSelectedIn to enforce a fix direction because of listener fire order
    void setSelected(@Nullable Selectable selected) {
        Selectable lastSelected = this.selected.get();

        if (lastSelected == selected) {
            return;
        }

        this.selected.set(selected);

        if (lastSelected != null) {
            lastSelected.setSelectedIn(null);
        }

        if (selected != null) {
            selected.setSelectedIn(this);
        }
    }

    public Hoverable getHovered() {
        return hovered.get();
    }

    public ObservableObjectValue<Hoverable> hoveredProperty() {
        return hovered;
    }

    void setHovered(Hoverable hovered) {
        Hoverable lastHovered = this.hovered.get();

        if (lastHovered == hovered) {
            return;
        }

        this.hovered.set(hovered);

        if (lastHovered != null) {
            lastHovered.setHoveredIn(null);
        }

        if (hovered != null) {
            hovered.setHoveredIn(this);
        }
    }

    public void clearSelection() {setSelected(null);}

    public boolean isPhase(Phase phase) {
        return phase.name().equals(getPhase());
    }

    public enum Phase {
        movePhase,
        attackPhase,
        lastMovePhase
    }


    public boolean isGameStarted() {
        return gameStarted.get();
    }

    public SimpleBooleanProperty gameStartedProperty() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted.set(gameStarted);
    }
}
