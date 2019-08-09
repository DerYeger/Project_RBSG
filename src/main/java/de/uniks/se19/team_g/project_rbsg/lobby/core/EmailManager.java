package de.uniks.se19.team_g.project_rbsg.lobby.core;

import com.sun.javafx.PlatformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class EmailManager
{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String EMAIL_ADDRESS = "seb@uni-kassel.de";
    private final String TOPIC = "Bug%20Report";
    private final String COMMAND = "mailto:" + EMAIL_ADDRESS + "?subject=" + TOPIC;
    private final String COMMAND_WINDOWS = "cmd /c start " + COMMAND;
    private final String COMMAND_MAC = "open " + COMMAND;
    private final String COMMAND_UNIX = "xdg-open " + COMMAND;

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
            rt.exec(cmd);
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }
}
