package de.uniks.se19.team_g.project_rbsg.server.websocket;

/**
 * @author Georg Siebert
 */

public interface IWebSocketCallback
{
    void handle(String message);
}
