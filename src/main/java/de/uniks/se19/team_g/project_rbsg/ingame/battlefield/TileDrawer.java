package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingOne;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingTwo;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * @author Georg Siebert
 */

public class TileDrawer
{
    private static final double CELL_SIZE = 64;
    private static final Color transparentWhite = Color.rgb(255, 255, 255, 0.2);
    private static final Color movementBlue = Color.rgb(134,140,252, 0.4);
    private static final Color selectedWhite = Color.rgb(255, 255, 255, 0.4);
    private static final Color movementBlueBorder = Color.rgb(134, 140, 252);
    private static final Color selectedBlue = Color.rgb(134,140,252);

    private static final Color attackRed = Color.rgb(207,102,121, 0.4);
    private static final Color attackRedBorder = Color.rgb(207,102,121);
    private static Image grass = new Image("/assets/cells/grass/grass1.png");

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private HashMap<UnitTypeInfo, Image> unitImagesMap;

    public TileDrawer()
    {
        unitImagesMap = new HashMap<>();

        for (UnitTypeInfo type : UnitTypeInfo.values())
        {
            Image image = new Image(type.getImage().toExternalForm(), CELL_SIZE, CELL_SIZE, false, true);
            unitImagesMap.put(type, image);
        }
    }

    @SuppressWarnings("unused")
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
        for (Tile[] tiles : map) {
            for (Tile tile : tiles) {
                drawTile(tile);
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
        if (tile.getDeckoratorImage() != null)
        {
            graphicsContext.drawImage(tile.getDeckoratorImage(), startX, startY);
        }
        //Layer 3 Highlighting One -> Move and Attack
        if (tile.getHighlightingOne() != HighlightingOne.NONE) {
            if (tile.getHighlightingOne() == HighlightingOne.MOVE) {
                graphicsContext.setFill(movementBlue);
                graphicsContext.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
                drawBorderAroundTile(startX, startY, movementBlueBorder);
            }
            if (tile.getHighlightingOne() == HighlightingOne.ATTACK) {
                graphicsContext.setFill(attackRed);
                graphicsContext.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
                drawBorderAroundTile(startX, startY, attackRedBorder);
            }
        }

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
            if (tile.getHighlightingTwo() == HighlightingTwo.SELECETD_WITH_UNITS)
            {
                graphicsContext.setFill(selectedWhite);
                graphicsContext.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
                drawBorderAroundTile(startX, startY, selectedBlue);
            }




        }

        //Layer 5
        if (tile.getCell().unitProperty().get() != null)
        {
            graphicsContext.drawImage(unitImagesMap.get(tile.getCell().getUnit().getUnitType()), startX, startY);
        }
    }

    private void drawBorderAroundTile(double startX, double startY, Color borderColer) {
        graphicsContext.setStroke(borderColer);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeLine((startX + 1), (startY + 1), (startX + (CELL_SIZE - 1)), (startY + 1));
        graphicsContext.strokeLine((startX + (CELL_SIZE - 1)), (startY + 1), startX + (CELL_SIZE - 1), startY + (CELL_SIZE - 1));
        graphicsContext.strokeLine((startX  + (CELL_SIZE - 1)), startY + (CELL_SIZE - 1), (startX + 1),startY + (CELL_SIZE - 1));
        graphicsContext.strokeLine((startX + 1), (startY + (CELL_SIZE - 1)), (startX + 1), (startY + 1));
    }
}
