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

import java.util.*;

public class MovementAnimationManager
{

    private static final double CELL_SIZE = 64.0;
    private HashMap<UnitTypeInfo, Image> imageHashMap;


    private AnimationTimer timer;
    private Unit oldUnit;
    private Runnable mapRedraw;

    public void setMapRedraw (Runnable mapRedraw)
    {
        this.mapRedraw = mapRedraw;
        imageHashMap = new HashMap<>();
    }

    public void startMovementAnimation (@NonNull Canvas canvas, @NonNull Unit unit, @NonNull Cell oldCell,
                                        @NonNull Cell newCell)
    {
        stopOldTimer();

        this.oldUnit = unit;

        addUnitToHashMap();

        unit.setDisplayed(false);
        Platform.runLater( () -> {
            mapRedraw.run();
        });

        DoubleProperty x = new SimpleDoubleProperty();
        DoubleProperty y = new SimpleDoubleProperty();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                             new KeyValue(x, oldCell.getX() * CELL_SIZE),
                             new KeyValue(y, oldCell.getY() * CELL_SIZE)),
                new KeyFrame(Duration.seconds(2),
                             new KeyValue(x, newCell.getX() * CELL_SIZE),
                             new KeyValue(y, newCell.getY() * CELL_SIZE))
        );

        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.setOnFinished(this::movementFinished);

        timer = new AnimationTimer()
        {
            @Override
            public void handle (long now)
            {
                Platform.runLater(() -> {
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    mapRedraw.run();
                    gc.drawImage(imageHashMap.get(unit.getUnitType()), x.doubleValue(),
                                 y.doubleValue());
                });
            }
        };

        timer.start();
        timeline.play();
    }

    private void stopOldTimer ()
    {
        if (timer != null)
        {
            timer.stop();
            if (oldUnit != null)
            {
                oldUnit.setDisplayed(true);
            }
        }
    }

    private void addUnitToHashMap ()
    {
        if(oldUnit != null && !imageHashMap.containsKey(oldUnit.getUnitType())) {
            imageHashMap.put(oldUnit.getUnitType(), new Image(oldUnit.getUnitType().getImage().toExternalForm(),
                                                              CELL_SIZE, CELL_SIZE, false, true));
        }
    }

    private void movementFinished (ActionEvent actionEvent)
    {
        oldUnit.setDisplayed(true);
        timer.stop();
        Platform.runLater( () -> {
            mapRedraw.run();
        });
    }
}
