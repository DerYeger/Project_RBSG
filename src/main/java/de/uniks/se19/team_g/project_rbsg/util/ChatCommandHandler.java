package de.uniks.se19.team_g.project_rbsg.util;

import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
@FunctionalInterface
public interface ChatCommandHandler {

    boolean handleCommand(@NonNull final ChatTabContentController callback, @NonNull final String command, @Nullable final String[] options) throws Exception;
}
