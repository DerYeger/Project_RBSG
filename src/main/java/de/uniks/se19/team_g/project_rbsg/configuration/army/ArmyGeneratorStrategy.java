package de.uniks.se19.team_g.project_rbsg.configuration.army;

import de.uniks.se19.team_g.project_rbsg.model.Army;

import java.util.List;

public interface ArmyGeneratorStrategy {

    Army createArmy(List<Army> armies);
}
