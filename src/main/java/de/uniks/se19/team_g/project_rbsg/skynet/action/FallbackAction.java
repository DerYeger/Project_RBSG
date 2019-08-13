package de.uniks.se19.team_g.project_rbsg.skynet.action;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class FallbackAction implements Action {

    private final Action action;
    private FallbackAction nextFallbackAction;

    public static FallbackAction of(@NonNull final Action action) {
        return new FallbackAction(action);
    }

    protected FallbackAction(@NonNull final Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public FallbackAction setNextAction(@Nullable final FallbackAction nextFallbackAction) {
        this.nextFallbackAction = nextFallbackAction;
        return this;
    }

    public Optional<FallbackAction> getNextAction() {
        if (nextFallbackAction == null) {
            return Optional.empty();
        }
        return Optional.of(nextFallbackAction);
    }
}
