package de.uniks.se19.team_g.project_rbsg.ingame.state;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventHandler;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GameEventDispatcher implements GameEventHandler {


    private List<Adapter> adapters = new ArrayList<>();
    private List<Listener> listeners = new ArrayList<>();
    boolean listenersSorted = false;

    private ModelManager modelManager;

    @Override
    public void handle(@Nonnull ObjectNode message) {

        Optional<GameEvent> gameEvent = adapters.stream()
                .sorted(Comparator.comparingInt(Adapter::getPriority))
                .map(adapter -> adapter.apply(message))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
            ;

        gameEvent.ifPresent(this::dispatch);
    }

    private void dispatch(GameEvent e) {
        if (!listenersSorted) {
            listeners.sort(Comparator.comparingInt(Listener::getPriority));
            listenersSorted = true;
        }

        for (Listener listener : listeners) {
            listener.accept(e, modelManager);
        }
    }

    public void addAdapter(Adapter adapter) {
        adapters.add(adapter);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listenersSorted = false;
    }

    public void setModelManager(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public interface Adapter extends Function<ObjectNode, Optional<GameEvent>> {
        default int getPriority() {return 0;}
    }

    public interface Listener extends BiConsumer<GameEvent, ModelManager> {
        default int getPriority() {return 0;}
    }
}
