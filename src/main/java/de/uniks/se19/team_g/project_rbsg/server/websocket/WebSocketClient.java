package de.uniks.se19.team_g.project_rbsg.server.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Georg Siebert
 * @author Jan Müller
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
    private WebSocketCloseHandler webSocketCloseHandler;
    private Session session;
    private Timer noopTimer;

    private boolean isClosed = false;

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

    public void setCloseHandler(@Nullable final WebSocketCloseHandler webSocketCloseHandler) {
        this.webSocketCloseHandler = webSocketCloseHandler;
    }

    public void start( final @NotNull String endpoint, final @NotNull IWebSocketCallback wsCallback) throws Exception {
        this.noopTimer = new Timer();

        this.wsCallback = wsCallback;

        URI uri = new URI(BASE_URL + endpoint);
        ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
    }

    @OnOpen
    public void onOpen(final Session session)
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
                String messageString = new ObjectMapper().writeValueAsString(message);
                this.session.getBasicRemote().sendText(messageString);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(final Session session, final CloseReason reason)
    {
        this.session = null;
        logger.debug("WS" + session.getRequestURI() + " closed, " + reason.getReasonPhrase());
        this.noopTimer.cancel();
        isClosed = true;
        if (webSocketCloseHandler != null) webSocketCloseHandler.onSocketClosed(reason);

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
            isClosed = true;
            noopTimer.cancel();
        }
    }

    public boolean isClosed() {
        return isClosed;
    }
}
