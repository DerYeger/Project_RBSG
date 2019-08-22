package de.uniks.se19.team_g.project_rbsg.bots;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new NamedInheritableThreadLocal<>("user context");

    public static void clearContext() {
        userContext.remove();
    }

    public static void setContext(@Nonnull UserContext userContext) {
        UserContextHolder.userContext.set(userContext);
    }

    @Nullable
    public static UserContext getContext() {
        return userContext.get();
    }
}
