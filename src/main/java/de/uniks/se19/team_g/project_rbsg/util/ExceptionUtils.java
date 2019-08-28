package de.uniks.se19.team_g.project_rbsg.util;

public class ExceptionUtils {

    public static Throwable rootCause(final Throwable throwable) {
        Throwable cause = throwable;

        while (cause != null && cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }
}
