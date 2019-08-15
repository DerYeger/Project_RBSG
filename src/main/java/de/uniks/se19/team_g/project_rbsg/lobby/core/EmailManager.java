package de.uniks.se19.team_g.project_rbsg.lobby.core;

import com.sun.javafx.PlatformUtil;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import io.rincl.Rincled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Component
public class EmailManager implements Rincled
{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String EMAIL_ADDRESS = "seb@uni-kassel.de";
    private final String TOPIC = "Bug%20Report";
    private final String COMMAND = "mailto:" + EMAIL_ADDRESS + "?subject=" + TOPIC;
    private final String COMMAND_WINDOWS = "cmd /c start " + COMMAND;
    private final String COMMAND_MAC = "open " + COMMAND;
    private final String COMMAND_UNIX = "xdg-open " + COMMAND;

    private final ApplicationState appState;

    private Process process = null;

    public EmailManager(ApplicationState appState)
    {
        this.appState = appState;
    }

    public void mailTo()
    {
        Desktop desktop;

        String cmd = COMMAND;

        if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL))
        {
            try
            {
                URI mailto = new URI(COMMAND);
                desktop.mail(mailto);
                return;
            } catch (URISyntaxException | IOException e)
            {
                e.printStackTrace();
            }
        }

        logger.debug("Desktop is not supported on this system.");

        if (PlatformUtil.isWindows())
        {
            cmd = COMMAND_WINDOWS;
        }
        else if (PlatformUtil.isLinux() || PlatformUtil.isUnix())
        {
            cmd = COMMAND_UNIX;
        }
        else if (PlatformUtil.isMac())
        {
            cmd = COMMAND_MAC;
        }

        Runtime rt = Runtime.getRuntime();
        try
        {
            process = rt.exec(cmd);
        } catch (IOException e)
        {
            if (Objects.nonNull(appState))
            {
                appState.notifications.add(getResources().getString("emailManager.error"));
            }
            e.printStackTrace();
        }


    }

    public Process getProcess()
    {
        return process;
    }
}
