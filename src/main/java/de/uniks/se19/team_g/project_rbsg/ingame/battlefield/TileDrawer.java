package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.beans.property.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import org.slf4j.*;

/**
 * @author Georg Siebert
 */

public class TileDrawer
{
    private static final double CELL_SIZE = 64;
    private static final int CELL_SIZE_INT = 64;
    private static final int HP_BORDER_OFFSET = 2;
    private static final int HP_BORDER_HEIGHT = 9;
    private static final int HP_BORDER_STROKE = 2;
    private static final int HP_BAR_HEIGHT = 5;


    private static final Color transparentWhite = Color.rgb(255, 255, 255, 0.2);
    private static final Color movementBlue = Color.rgb(134, 140, 252, 0.4);
    private static final Color selectedWhite = Color.rgb(255, 255, 255, 0.4);
    private static final Color movementBlueBorder = Color.rgb(134, 140, 252);
    private static final Color selectedBlue = Color.rgb(134, 140, 252);

    private static final Color attackRed = Color.rgb(207, 102, 121, 0.4);
    private static final Color attackRedBorder = Color.rgb(207, 102, 121);

    private static final Color attackBlocked = Color.web("737373", 0.5);
    private static final Color attackBlockedBorder = Color.web("737373");

    private static Image grass = new Image("/assets/cells/grass/grass1.png");

    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private SimpleBooleanProperty hpBarVisibility;

    public TileDrawer ()
    {
        hpBarVisibility = new SimpleBooleanProperty(true);
    }


    @SuppressWarnings("unused")
    public Canvas getCanvas ()
    {
        return canvas;
    }

    public void setCanvas (Canvas canvas)
    {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    public void drawMap (Tile[][] map)
    {
        for (Tile[] tiles : map)
        {
            for (Tile tile : tiles)
            {
                drawTile(tile);
            }
        }
    }

    public void drawTile (Tile tile)
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
        HighlightingOne highlightingOne = tile.getHighlightingOne();
        if (highlightingOne != HighlightingOne.NONE)
        {
            if (highlightingOne == HighlightingOne.MOVE)
            {
                drawTileFill(startX, startY, movementBlue);
                drawBorderAroundTile(startX, startY, movementBlueBorder);
            }
            if (highlightingOne == HighlightingOne.ATTACK)
            {
                drawTileFill(startX, startY, attackRed);
                drawBorderAroundTile(startX, startY, attackRedBorder);
            }
            if (highlightingOne == HighlightingOne.ATTACK_BLOCKED)
            {
                drawTileFill(startX, startY, attackBlocked);
                drawBorderAroundTile(startX, startY, attackBlockedBorder);
            }
        }

        //Layer 4 Highlighting Two -> Hovering and Selecting
        if (tile.getHighlightingTwo() != HighlightingTwo.NONE)
        {
            if (tile.getHighlightingTwo() == HighlightingTwo.HOVERED)
            {
                drawTileFill(startX, startY, transparentWhite);

            }
            if (tile.getHighlightingTwo() == HighlightingTwo.SELECTED)
            {
                drawTileFill(startX, startY, selectedWhite);
            }
            if (tile.getHighlightingTwo() == HighlightingTwo.SELECETD_WITH_UNITS)
            {
                drawTileFill(startX, startY, selectedWhite);
                drawBorderAroundTile(startX, startY, selectedBlue);
            }


        }

        Unit unit = tile.getCell().unitProperty().get();

        //Layer 5 Unit
        if (unit != null)
        {
            Image unitImage;

            if (unit.getUnitType() != null)
            {
                unitImage = unit.getUnitType().getPreview(CELL_SIZE_INT, CELL_SIZE_INT);
            }
            else {
                unitImage = UnitTypeInfo.UNKNOWN.getPreview(CELL_SIZE_INT, CELL_SIZE_INT);
            }

            graphicsContext.drawImage(unitImage, startX, startY);

            //Layer 6 HP Bar
            if (hpBarVisibility.get())
            {
                drawHealthBar(startX, startY, unit);
            }
        }


    }

    private void drawHealthBar (double startX, double startY, Unit unit)
    {
        //Drawing HP Bar Border
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(startX + HP_BORDER_OFFSET, startY + HP_BORDER_OFFSET, (CELL_SIZE - (HP_BORDER_OFFSET * 2)),
                                 HP_BORDER_HEIGHT);

        //Drawing HP Bar
        if (unit.getLeader() != null)
        {
            graphicsContext.setFill(Paint.valueOf(unit.getLeader().getColor()));
        }
        else
        {
            graphicsContext.setFill(Color.PINK);
        }

        double hpBarStartX = startX + HP_BORDER_OFFSET + HP_BORDER_STROKE;
        double hpBarStartY = startY + HP_BORDER_OFFSET + HP_BORDER_STROKE;
        double maxWidth = CELL_SIZE - ((HP_BORDER_OFFSET + HP_BORDER_STROKE) * 2);
        double hpPercentage = ((double) unit.getHp()) / ((double) unit.getMaxHp());
        double resultWidth = maxWidth * hpPercentage;

        graphicsContext.fillRect(hpBarStartX, hpBarStartY, resultWidth, HP_BAR_HEIGHT);
    }

    protected void drawTileFill (double startX, double startY, Color attackRed)
    {
        graphicsContext.setFill(attackRed);
        graphicsContext.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
    }

    private void drawBorderAroundTile (double startX, double startY, Color borderColer)
    {
        graphicsContext.setStroke(borderColer);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeLine((startX + 1), (startY + 1), (startX + (CELL_SIZE - 1)), (startY + 1));
        graphicsContext.strokeLine((startX + (CELL_SIZE - 1)), (startY + 1), startX + (CELL_SIZE - 1), startY + (CELL_SIZE - 1));
        graphicsContext.strokeLine((startX + (CELL_SIZE - 1)), startY + (CELL_SIZE - 1), (startX + 1), startY + (CELL_SIZE - 1));
        graphicsContext.strokeLine((startX + 1), (startY + (CELL_SIZE - 1)), (startX + 1), (startY + 1));
    }

    public boolean isHpBarVisibility ()
    {
        return hpBarVisibility.get();
    }

    public void setHpBarVisibility (boolean hpBarVisibility)
    {
        this.hpBarVisibility.set(hpBarVisibility);
    }

    public SimpleBooleanProperty hpBarVisibilityProperty ()
    {
        return hpBarVisibility;
    }
}
