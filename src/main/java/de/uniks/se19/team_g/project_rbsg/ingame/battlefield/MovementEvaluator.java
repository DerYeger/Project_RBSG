package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;
import java.util.stream.*;

@Component
public class MovementEvaluator
{

    @Nonnull
    public Map<Cell, Tour> getAllowedTours (Unit unit)
    {
        final HashMap<Cell, Tour> tours = new HashMap<>();

        final Cell startCell = unit.getPosition();

        updateNeighbors(startCell, unit, new ArrayList<>(), tours, 0);

        return tours;
    }

    private void updateNeighbors (
            @Nonnull Cell cell,
            Unit unit,
            ArrayList<Cell> path,
            HashMap<Cell, Tour> tours,
            int cost
    )
    {
        updateNeighbor(cell.getLeft(), unit, path, tours, cost);
        updateNeighbor(cell.getBottom(), unit, path, tours, cost);
        updateNeighbor(cell.getRight(), unit, path, tours, cost);
        updateNeighbor(cell.getTop(), unit, path, tours, cost);
    }

    private void updateNeighbor (
            Cell cell,
            Unit unit,
            ArrayList<Cell> path,
            HashMap<Cell, Tour> tours,
            final int costBefore
    )
    {
        if (cell == null)
        {
            return;
        }
        if (!cell.isPassable())
        {
            return;
        }

        // maybe the cost to move onto a cell will differ?
        int currentCost = costBefore + 1;

        if (currentCost > unit.getRemainingMovePoints())
        {
            // can't move here
            return;
        }

        final Tour tour;
        if (!tours.containsKey(cell))
        {
            tour = new Tour();
            tour.setTarget(cell);
            tour.setCost(Integer.MAX_VALUE);
            tours.put(cell, tour);
        }
        else
        {
            tour = tours.get(cell);
        }

        int previousCost = tour.getCost();
        if (currentCost < previousCost)
        {
            @SuppressWarnings("unchecked")
            ArrayList<Cell> nextPath = (ArrayList<Cell>) path.clone();
            nextPath.add(cell);
            tour.setPath(nextPath);
            tour.setCost(currentCost);
            updateNeighbors(cell, unit, nextPath, tours, currentCost);
        }
    }

    public Map<Cell, Tour> getValidTours (@NonNull final Unit unit)
    {
        return getAllowedTours(unit)
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getTarget().getUnit() == null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
