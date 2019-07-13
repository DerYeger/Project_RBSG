package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.HighlightingTwo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @Author Georg Siebert
 */

public class TileDrawer
{
    private static final double CELL_SIZE = 64;
    private static final Color transparentWhite = Color.rgb(255, 255, 255, 0.2);
    private static final Color selectedWhite = Color.rgb(255, 255, 255, 0.4);
    private static Image grass = new Image("/assets/cells/grass.png");
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Tile lastHovered;
    private Tile lastSelected;

    public TileDrawer()
    {
        lastHovered = null;
        lastSelected = null;
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

    public void drawMap(Tile[][] map)
    {
        for (int y = 0; y < map.length; y++)
        {
            for (int x = 0; x < map[y].length; x++)
            {
                drawTile(map[y][x]);
            }
        }
    }

    public void drawTile(Tile tile)
    {
        int x = tile.getCell().getX();
        int y = tile.getCell().getY();
        double startX = x * CELL_SIZE;
        double startY = y * CELL_SIZE;
        //Layer 0 default layer
        graphicsContext.drawImage(grass, startX, startY);
        //Layer 1 biome layer
        graphicsContext.drawImage(tile.getBackgroundImage(), startX, startY);
        //Layer 2 decorator layer
        if (tile.getDeckoratorImage() != null) {
            graphicsContext.drawImage(tile.getDeckoratorImage(), startX, startY);
        }
        //Layer 3 Highlighting One -> Move and Attack


        //Layer 4 Highlighting Two -> Hovering and Selecting
        if (tile.getHighlightingTwo() != HighlightingTwo.NONE)
        {
            if (tile.getHighlightingTwo() == HighlightingTwo.HOVERED)
            {
                graphicsContext.setFill(transparentWhite);
                graphicsContext.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
            }
            if (tile.getHighlightingTwo() == HighlightingTwo.SELECTED)
            {
                graphicsContext.setFill(selectedWhite);
                graphicsContext.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
            }
        }

        //Layer 5
        if (tile.getCell().getUnit().get() != null)
        {
            String imagePath = UnitTypeInfo.UNKNOWN.getImage().toExternalForm();
            Image unitImage = new Image(imagePath, CELL_SIZE, CELL_SIZE, false, true);
            graphicsContext.drawImage(unitImage, startX, startY);
        }
    }
}
