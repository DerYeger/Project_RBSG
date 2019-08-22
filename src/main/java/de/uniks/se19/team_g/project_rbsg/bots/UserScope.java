package de.uniks.se19.team_g.project_rbsg.bots;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.annotation.Nonnull;

public class UserScope implements Scope {

    public static final String SCOPE_NAME = "user";

    @Override
    public Object get(@Nonnull String name, @Nonnull ObjectFactory<?> objectFactory) {
        return getUserContext().getScopedObjects()
                .computeIfAbsent(name, key -> objectFactory.getObject());
    }

    @Override
    public Object remove(@Nonnull String name) {
        getUserContext().getDestructionCallbacks().remove(name);
        return getUserContext().getScopedObjects()
                .remove(name)
        ;
    }

    @Override
    public void registerDestructionCallback(@Nonnull String name, @Nonnull Runnable callback) {
        getUserContext().getDestructionCallbacks()
                .put(name, callback);
    }

    @Override
    public Object resolveContextualObject(@Nonnull String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    @Nonnull
    protected UserContext getUserContext() {
        UserContext userContext = UserContextHolder.getContext();
        if (userContext == null) {
            userContext = new UserContext();
            UserContextHolder.setContext(userContext);
        }
        return userContext;
    }
}
