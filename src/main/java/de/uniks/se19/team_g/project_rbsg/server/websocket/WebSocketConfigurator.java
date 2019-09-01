package de.uniks.se19.team_g.project_rbsg.server.websocket;

import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import org.springframework.beans.factory.ObjectFactory;

import javax.websocket.ClientEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Georg Siebert
 */

public class WebSocketConfigurator extends ClientEndpointConfig.Configurator
{
    public static UserProvider userProvider = null;

    @Override
    public void beforeRequest(final Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        ArrayList<String> key = new ArrayList<>();
        key.add(userProvider.get().getUserKey());
        headers.put("userKey", key);
    }
}
