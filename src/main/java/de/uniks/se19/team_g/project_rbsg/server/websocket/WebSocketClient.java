package de.uniks.se19.team_g.project_rbsg.lobby.Logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.lobby.Logic.Contract.IWebSocketCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

@Component
@Scope("prototype")
@ClientEndpoint(configurator = WebSocketConfigurator.class)
public class WebSocketClient
{
    private final static String BASE_URL = "wss://rbsg.uniks.de/ws";
    private final static int TIMER_PERIOD = 40000;
    private final static int TIMER_DELAY = 0;
    private final static String NOOP = "noop";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IWebSocketCallback wsCallback;
    private Session session;
    private Timer noopTimer;

    private TimerTask timerTask = new TimerTask()
    {
        @Override
        public void run()
        {
            if (session.isOpen())
            {
                try
                {
                    session.getBasicRemote().sendText(NOOP);
                    logger.debug("Send NOOP");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    logger.warn("Can not send NOOP");
                }
            }
        }
    };

    public WebSocketClient()
    {
        this.noopTimer = new Timer();
    }

    public void start( final @NotNull String endpoint, final @NotNull IWebSocketCallback wsCallback)
    {
        this.wsCallback = wsCallback;
        try
        {
            URI uri = new URI(BASE_URL + endpoint);
            ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
        }
        catch (DeploymentException | IOException | URISyntaxException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(final Session session) throws IOException
    {
        this.session = session;
        logger.debug("WS connected to " + session.getRequestURI());
        this.noopTimer.schedule(timerTask, TIMER_DELAY, TIMER_PERIOD);
    }

    @OnMessage
    public void onMessage(final String message, final Session session) throws IOException
    {
        if (wsCallback != null)
        {
            wsCallback.handle(message);
        }
        else
        {
            logger.warn("No Callback available");
        }
    }

    public void sendMessage(final Object message)
    {
        if (this.session != null && this.session.isOpen())
        {
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
    public void onClose(final Session session, final CloseReason reason) throws IOException
    {
        this.session = null;
        logger.debug("WS" + session.getRequestURI() + " closed, " + reason.getReasonPhrase());

        this.noopTimer.cancel();
    }

    public void stop()
    {
        if (this.session != null && this.session.isOpen())
        {
            try
            {
                this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Tschau"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            noopTimer.cancel();
        }
    }
}
