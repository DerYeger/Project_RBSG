package de.uniks.se19.team_g.project_rbsg.scene;

import org.springframework.lang.NonNull;

@FunctionalInterface
public interface ExceptionHandler {
    void handle(@NonNull final Exception exception);
}
