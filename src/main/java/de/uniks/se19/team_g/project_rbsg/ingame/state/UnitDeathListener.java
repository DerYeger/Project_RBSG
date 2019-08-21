package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class UnitDeathListener implements GameEventDispatcher.Listener {

    private Map<Unit, Map<String, CompletableFuture<Void>>> expectations = new HashMap<>();

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher dispatcher) {
        if (!(gameEvent instanceof GameRemoveObjectEvent)) {
            return;
        }

        Object entity = dispatcher.getModelManager().getEntityById(((GameRemoveObjectEvent) gameEvent).entityId);

        if (!(entity instanceof Unit)) {
            return;
        }

        Map<String, CompletableFuture<Void>> expectationsForUnit = expectations.computeIfAbsent((Unit) entity, unit -> waitForEvents(unit, dispatcher));

        String key = getKeyFor((GameRemoveObjectEvent) gameEvent);
        Optional.ofNullable(expectationsForUnit.get(key))
            .ifPresent(future -> future.complete(null));
    }

    // basically anti collision stuff
    private String getKeyFor(GameRemoveObjectEvent gameEvent) {
        String type = ModelManager.splitIdentifier(gameEvent.fromId).first;

        if ("Game".equals(type)) {
            return type + "@" + gameEvent.fieldName;
        }

        return gameEvent.fieldName;
    }

    private Map<String, CompletableFuture<Void>> waitForEvents(Unit unit, GameEventDispatcher dispatcher) {
        Player leader = unit.getLeader();

        Map<String, CompletableFuture<Void>> expectationsForUnit = new HashMap<>();
        expectationsForUnit.put("Game@"+Game.UNITS, new CompletableFuture<>());
        expectationsForUnit.put(Cell.UNIT, new CompletableFuture<>());
        expectationsForUnit.put(Player.UNITS, new CompletableFuture<>());

        if (leader != null) {
            CompletableFuture.allOf(expectationsForUnit.values().toArray(CompletableFuture[]::new))
                    .thenRun(() -> publishUnitDeath(unit, leader, dispatcher))
                    .thenRun(() -> expectations.remove(unit))
            ;
        }

        return expectationsForUnit;
    }

    private void publishUnitDeath(Unit unit, Player leader, GameEventDispatcher dispatcher) {
        dispatcher.getModelManager().addAction(
            new UnitDeathAction(unit, leader)
        );
    }
}
