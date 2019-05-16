package de.uniks.se19.team_g.project_rbsg.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface ChatCommandHandler {

    boolean handleCommand(@NonNull final String command, @Nullable final String[] options) throws Exception;
}
