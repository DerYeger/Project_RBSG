package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic;

import javax.websocket.ClientEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebSocketConfigurator extends ClientEndpointConfig.Configurator
{

    public static String userKey = "";

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        ArrayList<String> key = new ArrayList<>();
        key.add(userKey);
        headers.put("userKey", key);

    }

}
