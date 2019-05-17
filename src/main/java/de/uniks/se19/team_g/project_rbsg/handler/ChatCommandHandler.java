package de.uniks.se19.team_g.project_rbsg.handler;

import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
@FunctionalInterface
public interface ChatCommandHandler {

    void handleCommand(@NonNull final ChatTabContentController callback, @Nullable final String options) throws Exception;
}
