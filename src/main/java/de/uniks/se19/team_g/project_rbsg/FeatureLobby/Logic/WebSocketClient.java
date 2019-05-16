package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.IWSCallback;

import javax.validation.constraints.NotNull;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Georg Siebert
 */

@ClientEndpoint(configurator = WebSocketConfigurator.class)
public class WebSocketClient
{
    private final static String BASE_URL = "wss://rbsg.uniks.de/ws";
    private final static int TIMER_PERIOD = 60000;
    private final static int TIMER_DELAY = 0;
    private final static String NOOP = "noop";

    private IWSCallback wsCallback;
    private Session session;
    private Timer noopTimer;

    public WebSocketClient(final @NotNull String endpoint, final @NotNull IWSCallback wsCallback)
    {
        this.noopTimer = new Timer();
        this.wsCallback = wsCallback;

        try
        {
            URI uri = new URI(BASE_URL + endpoint);
            ContainerProvider.getWebSocketContainer().connectToServer(this ,uri);
        }
        catch (DeploymentException | IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException{
        this.session = session;
        System.out.println("WS connected to" + this.session.getRequestURI());

        this.noopTimer.schedule(timerTask, TIMER_DELAY, TIMER_PERIOD);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        wsCallback.handle(message);
    }

    public void sendMessage(final Object message) {
        if(this.session != null && this.session.isOpen()) {
            try
            {
                this.session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        this.session = null;
        System.out.println("WS" + session.getRequestURI() + " closed, " + reason.getReasonPhrase());

        this.noopTimer.cancel();
    }

        private TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if(session.isOpen()) {
                    try
                    {
                        session.getBasicRemote().sendText(NOOP);                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        System.err.println("Can not send NOOP");
                    }
                }
            }
        };
}
