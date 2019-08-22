package de.uniks.se19.team_g.project_rbsg.bots;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.annotation.Nonnull;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new NamedInheritableThreadLocal<>("user context");
    private static UserContext defaultContext = new UserContext();

    public static void clearContext() {
        userContext.remove();
    }

    public static void setContext(@Nonnull UserContext userContext) {
        UserContextHolder.userContext.set(userContext);
    }

    @Nonnull
    public static UserContext getContext() {
        UserContext userContext = UserContextHolder.userContext.get();
        return userContext != null ? userContext : UserContextHolder.defaultContext;
    }
}
