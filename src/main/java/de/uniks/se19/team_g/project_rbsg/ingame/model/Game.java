package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

public class Game {

    @NonNull
    private final String id;

    private ArrayList<Player> players;

    private ArrayList<Unit> units;

    private ArrayList<Cell> cells;

    public Game(@NonNull final String id) {
        this.id = id;

        players = new ArrayList<>();
        units = new ArrayList<>();
        cells = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Cell> getCells() {
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
        if (players.contains(player)) return this;
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
        if (units.contains(unit)) return this;
        doAddUnit(unit);
        unit.doSetGame(this);
        return this;
    }

    void doAddUnit(@NonNull final Unit unit) {
        units.add(unit);
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

    @Override
    public String toString() {
        return "(" + id + ", " + players.size() + " players, " + units.size() + " units)";
    }
}
