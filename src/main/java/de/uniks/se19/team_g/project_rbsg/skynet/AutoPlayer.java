package de.uniks.se19.team_g.project_rbsg.skynet;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoPlayer implements Runnable
{
    public DoubleProperty frequency = new SimpleDoubleProperty(2);
    private Skynet skynet;
    private Thread myThread;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public AutoPlayer(Skynet skynet)
    {
        this.skynet = skynet;
    }

    public Thread start()
    {
        if (myThread != null)
        {
            logger.debug("Skynet Bot already started");
            return myThread;
        } else
        {
            logger.info("Starting Skynet Bot!");
            myThread = new Thread(this);
            myThread.start();
            return myThread;
        }
    }

    public void stop()
    {
        logger.info("Stopping Skynet Bot!");
        myThread = null;
    }

    public void run()
    {
        Thread currentThread = Thread.currentThread();
        while (currentThread == myThread)
        {
            Platform.runLater(() -> skynet.turn());
            try
            {
                Thread.sleep((int) (1000 / frequency.get()));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
