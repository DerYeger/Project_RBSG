package de.uniks.se19.team_g.project_rbsg.waiting_room.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

/**
 * @author Jan Müller
 */
public class Player {

    @NonNull
    private final String id;

    private Game game;

    private String name;

    private String color;

    private ObservableList<Unit> units;

    public Player(@NonNull final String id) {
        this.id = id;

        units = FXCollections.observableArrayList();
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
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

    public Player setGame(@Nullable final Game game) {
        if (this.game != null) this.game.doRemovePlayer(this);
        doSetGame(game);
        if (game != null) game.doAddPlayer(this);
        return this;
    }

    void doSetGame(@Nullable final Game game) {
        this.game = game;
    }

    public Player withUnits(@Nullable final Unit ...units) {
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


    public Player setName(@NonNull final String name) {
        this.name = name;
        return this;
    }

    public Player setColor(@NonNull final String color) {
        this.color = color;
        return this;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + color + ")";
    }
}
