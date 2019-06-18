package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;

@Component
public class ArmyUnitAdapter {

    @Nonnull
    private final ApplicationState appState;

    public ArmyUnitAdapter(@Nonnull ApplicationState appState) {
        this.appState = appState;
    }

    public Unit mapServerUnit(String id) {

        final Unit matchingDefinition = appState.unitDefinitions.stream()
            .filter(Objects::nonNull)
            .filter(other -> other.id.get().equals(id))
            .findAny()
            .orElse(null)
        ;

        final Unit unit;
        if (matchingDefinition != null) {
            unit = matchingDefinition.clone();
        } else {
            unit = Unit.unknownType(id);
        }

        return unit;
    }
}