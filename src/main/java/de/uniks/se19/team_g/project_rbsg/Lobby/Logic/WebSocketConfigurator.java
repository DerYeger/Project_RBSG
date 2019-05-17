package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import javax.websocket.ClientEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Georg Siebert
 */

public class WebSocketConfigurator extends ClientEndpointConfig.Configurator
{
    public static String userKey = "";

    @Override
    public void beforeRequest(final Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        ArrayList<String> key = new ArrayList<>();
        key.add(userKey);
        headers.put("userKey", key);

    }

}
