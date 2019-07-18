package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import org.slf4j.*;

public class MiniMapDrawer
{
    private final static Color darkgreen = Color.DARKGREEN;
    private final static Color green = Color.GREEN;
    private final static Color brown = Color.BROWN;
    private final static Color darkblue = Color.DARKBLUE;
    private final static Color white = Color.WHITE;
    private final static Color black = Color.BLACK;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Canvas canvas;
    private double xSize = 0;
    private double ySize = 0;
    private double CellSizeX = 0;
    private double CellSizeY = 0;

    private GraphicsContext gc;

    public MiniMapDrawer()
    {
    }

    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        xSize = canvas.getWidth();
        ySize = canvas.getHeight();
    }

    public void drawMinimap(Tile[][] map)
    {
        if (canvas == null)
        {
            logger.error("Canvas is not set!");
            return;
        }

        CellSizeX = xSize / map.length;
        CellSizeY = ySize / map.length;

        for (int y = 0; y < map.length; y++)
        {
            for (int x = 0; x < map[y].length; x++)
            {
                double startX = x * CellSizeX;
                double startY = y * CellSizeY;
                Tile actualTile = map[y][x];

                if (actualTile.getHighlightingTwo() == HighlightingTwo.SELECTED)
                {
                    gc.setStroke(white);
                }
                else
                {
                    switch (actualTile.getCell().getBiome())
                    {
                        case WATER:
                            gc.setStroke(darkblue);
                            break;
                        case GRASS:
                            gc.setStroke(green);
                            break;
                        case FOREST:
                            gc.setStroke(darkgreen);
                            break;
                        case MOUNTAIN:
                            gc.setStroke(brown);
                            break;
                        default:
                            gc.setStroke(black);
                    }
                }
                gc.fillRect(startX, startY, CellSizeX, CellSizeY);

                if(actualTile.getCell().getUnit() != null) {
                    double startUnitRecX = startX + (CellSizeX/4);
                    double startUnitRecY = startY + (CellSizeY/4);

                    gc.setStroke(Color.valueOf(actualTile.getCell().getUnit().get().getLeader().getColor()));
                    gc.fillRect(startUnitRecX, startUnitRecY, CellSizeX/2, CellSizeY/2);
                }
            }
        }
    }

}
