package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.animations;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.util.*;
import org.springframework.lang.*;

public class RevivalAnimationManager
{
    private static final double CELL_SIZE = 64.0;

    private final Image revivalImage = UnitImageResolver.getRevivalImage();

    private Unit targetUnit;
    private AnimationTimer timer;
    private Runnable mapRedraw;

    public void setMapRedraw (Runnable mapRedraw)
    {
        this.mapRedraw = mapRedraw;
    }

    public void startRevivalAnimation (@NonNull Canvas canvas, @NonNull Unit unit)
    {
        stopOldTimer();


        Cell cell = unit.getPosition();

        if (cell == null)
        {
            return;
        }

        if (mapRedraw == null)
        {
            return;
        }

        Platform.runLater(() ->
                          {
                              mapRedraw.run();
                          });

        DoubleProperty x = new SimpleDoubleProperty();
        DoubleProperty y = new SimpleDoubleProperty();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                             new KeyValue(x, cell.getX() * CELL_SIZE),
                             new KeyValue(y, cell.getY() * CELL_SIZE)),
                new KeyFrame(Duration.seconds(1),
                             new KeyValue(x, cell.getX() * CELL_SIZE),
                             new KeyValue(y, cell.getY() * CELL_SIZE))
        );

        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.setOnFinished(this::revivalFinished);

        targetUnit = unit;
        unit.setDisplayed(false);

        timer = new AnimationTimer()
        {
            @Override
            public void handle (long now)
            {
                Platform.runLater(() ->
                                  {
                                      GraphicsContext gc = canvas.getGraphicsContext2D();
                                      mapRedraw.run();
                                      gc.drawImage(revivalImage, x.doubleValue(),
                                                   y.doubleValue());
                                  });
            }
        };

        timer.start();
        timeline.play();
    }

    private void revivalFinished (ActionEvent actionEvent)
    {
        timer.stop();
        targetUnit.setDisplayed(true);
        Platform.runLater(() ->
                          {
                              mapRedraw.run();
                          });
    }

    private void stopOldTimer ()
    {
        if (timer != null)
        {
            timer.stop();
            if (targetUnit != null)
            {
                targetUnit.setDisplayed(true);
            }
        }
    }
}
