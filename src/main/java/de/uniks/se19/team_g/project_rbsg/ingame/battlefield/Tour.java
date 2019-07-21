package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class Tour {

    private Cell target;
    private int cost;
    private List<Cell> path;

    public Cell getTarget() {
        return target;
    }

    public List<Cell> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

    public void setTarget(Cell cell) {

        this.target = cell;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPath(List<Cell> path) {

        this.path = path;
    }
}
