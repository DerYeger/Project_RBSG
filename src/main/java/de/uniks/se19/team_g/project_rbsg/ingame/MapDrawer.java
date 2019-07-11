package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.Camera;
import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MapDrawer
{
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    public MapDrawer() {

    }


    public Canvas getCanvas()
    {
        return canvas;
    }

    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    public void drawMap(Tile[][] map, Camera camera) {
        for (int y = camera.getyPos(); y < camera.getyPos() + camera.getViewSize(); y++)
        {
            for (int x = camera.getxPos(); x < camera.getxPos() + camera.getViewSize(); x++)
            {

            }
        }
    }

}
