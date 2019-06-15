package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashSet;

public class Game {

    @NonNull
    private final String id;

    private HashSet<Player> players;

    private HashSet<Unit> units;

    private HashSet<Cell> cells;

    public Game(@NonNull final String id) {
        this.id = id;

        players = new HashSet<>();
        units = new HashSet<>();
        cells = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public HashSet<Player> getPlayers() {
        return players;
    }

    public HashSet<Unit> getUnits() {
        return units;
    }

    public HashSet<Cell> getCells() {
        return cells;
    }

    public Game withPlayers(@Nullable final Player ...players)
    {
        if (players != null) {
            for (final Player player : players) {
                if (player != null) withPlayer(player);
            }
        }
        return this;
    }

    public Game withPlayer(@NonNull final Player player) {
        doAddPlayer(player);
        player.doSetGame(this);
        return this;
    }

    void doAddPlayer(@NonNull final Player player) {
        players.add(player);
    }

    public Game withUnits(@Nullable final Unit ...units) {
        if (units != null) {
            for (final Unit unit : units) {
                if (unit != null) withUnit(unit);
            }
        }
        return this;
    }

    public Game withUnit(@NonNull final Unit unit) {
        doAddUnit(unit);
        unit.doSetGame(this);
        return this;
    }

    void doAddUnit(@NonNull final Unit unit) {
        units.add(unit);
    }

    public Game withCell(@NonNull final Cell cell) {
        doAddCell(cell);
        cell.doSetGame(this);
        return this;
    }

    void doAddCell(@NonNull final Cell cell) {
        cells.add(cell);
    }

    @Override
    public String toString() {
        return "(" + id + ", " + players.size() + " players, " + units.size() + " units)";
    }
}
