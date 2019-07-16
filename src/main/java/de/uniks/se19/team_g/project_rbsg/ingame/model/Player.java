package de.uniks.se19.team_g.project_rbsg.ingame.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Jan Müller
 */
public class Player {

    @NonNull
    private final String id;

    private Game currentGame;

    private String name;

    private String color;

    private ObservableList<Unit> units;

    final private BooleanProperty isReady = new SimpleBooleanProperty(false);

    final private BooleanProperty isCurrentPlayer = new SimpleBooleanProperty(false);

    public Player(@NonNull final String id) {
        this.id = id;

        units = FXCollections.observableArrayList();
    }

    public String getId() {
        return id;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public ObservableList<Unit> getUnits() {
        return units;
    }

    public Player setCurrentGame(@Nullable final Game currentGame) {
        if (this.currentGame == currentGame) return this;
        if (this.currentGame != null) this.currentGame.doRemovePlayer(this);
        doSetGame(currentGame);
        if (currentGame != null) currentGame.doAddPlayer(this);
        return this;
    }

    void doSetGame(@Nullable final Game game) {
        this.currentGame = game;
    }

    public Player withUnits(@Nullable final Unit ...units) {
        if (units == null) return this;
        return withUnits(Arrays.asList(units));
    }

    public Player withUnits(@Nullable final Collection<Unit> units) {
        if (units != null) {
            for (final Unit unit : units) {
                if (unit != null) withUnit(unit);
            }
        }
        return this;
    }

    public Player withUnit(@NonNull final Unit unit) {
        if (units.contains(unit)) return this;
        doAddUnit(unit);
        unit.doSetLeader(this);
        return this;
    }

    void doAddUnit(@NonNull final Unit unit) {
        units.add(unit);
    }

    public Player withoutUnits(@Nullable final Unit ...units) {
        if (units == null) return this;
        return withoutUnits(Arrays.asList(units));
    }

    public Player withoutUnits(@Nullable final Collection<Unit> units) {
        if (units != null) {
            for (final Unit unit : units) {
                if (unit != null) withoutUnit(unit);
            }
        }
        return this;
    }

    public Player withoutUnit(@NonNull final Unit unit) {
        if (!units.contains(unit)) return this;
        doRemoveUnit(unit);
        unit.doSetLeader(null);
        return this;
    }

    void doRemoveUnit(@NonNull final Unit unit) {
        units.remove(unit);
    }

    public Player setName(@NonNull final String name) {
        this.name = name;
        return this;
    }

    public Player setColor(@NonNull final String color) {
        this.color = color;
        return this;
    }

    public void remove() {
        setCurrentGame(null);
        withoutUnits(new ArrayList<>(units));
    }

    @Override
    public String toString() {
        return "(" + name + ", " + color + ")";
    }

    public boolean getIsReady() {
        return isReady.get();
    }

    public ReadOnlyBooleanProperty isReadyProperty() {
        return isReady;
    }

    public Player setIsReady(boolean isReady) {
        this.isReady.set(isReady);
        return this;
    }


}
