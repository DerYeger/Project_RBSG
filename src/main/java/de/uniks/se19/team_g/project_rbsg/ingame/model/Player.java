package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashSet;

public class Player {

    private final String id;

    private Game game;

    private String name;

    private String color;

    private HashSet<Unit> units;

    public Player(@NonNull final String id) {
        this.id = id;

        units = new HashSet<>();
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

    public HashSet<Unit> getUnits() {
        return units;
    }

    public Player setGame(@NonNull final Game game) {
        doSetGame(game);
        game.doAddPlayer(this);
        return this;
    }

    void doSetGame(@NonNull final Game game) {
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
}
