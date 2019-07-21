package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingTwo;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniMapDrawer
{
    private final static Color darkgreen = Color.DARKGREEN;
    private final static Color green = Color.GREEN;
    private final static Color brown = Color.BROWN;
    private final static Color darkblue = Color.DARKBLUE;
    private final static Color white = Color.WHITE;
    private final static Color black = Color.BLACK;
    private final static Color transparentWhite = Color.rgb(255, 255, 255, 0.5);
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

    public void setCanvas(Canvas canvas, int mapSize)
    {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        xSize = canvas.getWidth();
        ySize = canvas.getHeight();

        CellSizeX = Math.round(xSize / mapSize);
        CellSizeY = Math.round(ySize / mapSize);
        double width = CellSizeX * mapSize;
        double height = CellSizeY * mapSize;
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    public void drawMinimap(Tile[][] map)
    {
        if (canvas == null)
        {
            logger.error("Canvas is not set!");
            return;
        }

        if (!canvas.visibleProperty().get())
        {
            return;
        }

        CellSizeX = Math.round(xSize / map.length);
        CellSizeY = Math.round(ySize / map.length);

        for (int y = 0; y < map.length; y++)
        {
            for (int x = 0; x < map[y].length; x++)
            {
                double startX = x * CellSizeX;
                double startY = y * CellSizeY;
                Tile actualTile = map[y][x];

                if (actualTile.getHighlightingTwo() == HighlightingTwo.SELECTED)
                {
                    gc.setFill(white);
                }
                else if(actualTile.getHighlightingTwo() == HighlightingTwo.HOVERED)
                {
                    gc.setFill(transparentWhite);
                }
//                else if(actualTile.getHighlightingTwo() == HighlightingTwo.UnitSelcted) {
//                    gc.setFill(primaryColor);
//                }
                else
                {
                    switch (actualTile.getCell().getBiome())
                    {
                        case WATER:
                            gc.setFill(darkblue);
                            break;
                        case GRASS:
                            gc.setFill(green);
                            break;
                        case FOREST:
                            gc.setFill(darkgreen);
                            break;
                        case MOUNTAIN:
                            gc.setFill(brown);
                            break;
                        default:
                            gc.setFill(black);
                    }
                }
                gc.fillRect(startX, startY, CellSizeX, CellSizeY);

                if (actualTile.getCell().getUnit() != null)
                {
                    double startUnitRecX = startX + (CellSizeX / 4);
                    double startUnitRecY = startY + (CellSizeY / 4);

                    if (actualTile.getCell().getUnit().getLeader() != null)
                    {
                        gc.setFill(Color.valueOf(actualTile.getCell().getUnit().getLeader().getColor()));
                    } else
                    {
                        gc.setFill(Color.RED);
                    }
                    gc.fillRect(startUnitRecX, startUnitRecY, Math.round(CellSizeX / 2), Math.round(CellSizeY / 2));
                }
            }
        }
    }

}
