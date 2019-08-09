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
    private final String COMMAND =  "mailto:" + EMAIL_ADDRESS + "?subject=" + TOPIC;
    private final String COMMAND_WINDOWS = "cmd /c start " + COMMAND;

    public void mailTo()
    {
        Desktop desktop;

        if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL))
        {
            try
            {
                URI mailto = new URI(COMMAND);
                desktop.mail(mailto);
            } catch (URISyntaxException | IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            logger.debug("Desktop is not supported on this system.");
            String os = System.getProperty("os.name");
            if (PlatformUtil.isWindows())
            {
                Runtime rt = Runtime.getRuntime();
                try
                {
                    Process pr = rt.exec(COMMAND_WINDOWS);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else if (PlatformUtil.isLinux())
            {

            }
            else if (PlatformUtil.isMac())
            {

            }

        }
    }
}
