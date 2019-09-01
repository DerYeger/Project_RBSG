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

public class DeathAnimationManager
{
    private static final double CELL_SIZE = 64.0;

    private final Image deathImage = UnitImageResolver.getDeathImage();

    private AnimationTimer timer;
    private Runnable mapRedraw;

    public void setMapRedraw (Runnable mapRedraw)
    {
        this.mapRedraw = mapRedraw;
    }

    public void startDeathAnimation (@NonNull Canvas canvas, @NonNull Cell cell)
    {
        stopOldTimer();

        if(mapRedraw == null) {
            return;
        }

        Platform.runLater(() -> {
            mapRedraw.run();
        });

        DoubleProperty x = new SimpleDoubleProperty();
        DoubleProperty y = new SimpleDoubleProperty();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                             new KeyValue(x, cell.getX() * CELL_SIZE),
                             new KeyValue(y, cell.getY() * CELL_SIZE)),
                new KeyFrame(Duration.seconds(1),
                             new KeyValue(x, cell.getX() * CELL_SIZE+1),
                             new KeyValue(y, cell.getY() * CELL_SIZE+1))
        );

        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.setOnFinished(this::deathFinished);

        timer = new AnimationTimer()
        {
            @Override
            public void handle (long now)
            {
                Platform.runLater(() -> {
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    mapRedraw.run();
                    gc.drawImage(deathImage, x.doubleValue(),
                                 y.doubleValue());
                });
            }
        };

        timer.start();
        timeline.play();
    }

    private void deathFinished (ActionEvent actionEvent)
    {
        timer.stop();
        Platform.runLater( () -> {
            mapRedraw.run();
        });
    }

    private void stopOldTimer ()
    {
        if (timer != null)
        {
            timer.stop();
        }
    }
}
