package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;

import java.util.HashMap;

public class Game {

    private String id;

    private HashMap<String, Cell> cells;

    public Game() {
        cells = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Game setId(String id) {
        this.id = id;
        return this;
    }

    public Game withCell(@NonNull final Cell cell) {
        doAddCell(cell);
        cell.doSetGame(this);
        return this;
    }

    public Game doAddCell(@NonNull final Cell cell) {
        cells.put(cell.getId(), cell);
        return this;
    }

    public HashMap<String, Cell> getCells() {
        return cells;
    }
}
