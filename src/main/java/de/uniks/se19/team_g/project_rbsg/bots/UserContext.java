package de.uniks.se19.team_g.project_rbsg.bots;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserContext {

    final private Map<String, Object> scopedObjects
            = Collections.synchronizedMap(new HashMap<String, Object>());
    final private Map<String, Runnable> destructionCallbacks
            = Collections.synchronizedMap(new HashMap<String, Runnable>());


    public Map<String, Object> getScopedObjects() {
        return scopedObjects;
    }

    public Map<String, Runnable> getDestructionCallbacks() {
        return destructionCallbacks;
    }
}
