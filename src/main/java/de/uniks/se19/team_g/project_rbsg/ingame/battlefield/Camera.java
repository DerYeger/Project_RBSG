package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import javafx.beans.property.*;
import javafx.beans.value.*;
import org.slf4j.*;

public class Camera
{
    private static final int CELL_SIZE = 64;
    private final int mapSize;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SimpleDoubleProperty zoomFactor;
    private SimpleDoubleProperty xFactor;
    private SimpleDoubleProperty yFactor;
    private SimpleDoubleProperty height;
    private SimpleDoubleProperty width;

    private int visibleCellsX;
    private int visibleCellsY;

    private int xStartCell;
    private int yStartCell;

    private double xOffset;
    private double yOffset;

    private int maxStartCellX;
    private int maxStartCellY;

    private ChangeListener<Number> factorListener;

    public Camera(SimpleDoubleProperty zoomFactor, DoubleProperty hValue, DoubleProperty vValue, int mapSize, ReadOnlyDoubleProperty height, ReadOnlyDoubleProperty width)
    {
        this.zoomFactor = new SimpleDoubleProperty();
        this.xFactor = new SimpleDoubleProperty();
        this.yFactor = new SimpleDoubleProperty();
        this.height = new SimpleDoubleProperty();
        this.width = new SimpleDoubleProperty();

        this.zoomFactor.bindBidirectional(zoomFactor);
        this.xFactor.bindBidirectional(hValue);
        this.yFactor.bindBidirectional(vValue);
        this.mapSize = mapSize;
        this.height.bind(height);
        this.width.bind(width);

        completeCalculation();

        factorListener = this::FactorChanged;

        zoomFactor.addListener(factorListener);
        xFactor.addListener(factorListener);
        yFactor.addListener(factorListener);
        height.addListener(factorListener);
        height.addListener(factorListener);
    }

    public int getMaxStartCellX()
    {
        return maxStartCellX;
    }

    public int getMaxStartCellY()
    {
        return maxStartCellY;
    }

    public void setToStartPos(int x, int y)
    {
        if (maxStartCellX < x || maxStartCellY < y)
        {
            return;
        }

        int startX = CELL_SIZE * x;
        int startY = CELL_SIZE * y;

        double xFactor = startX / xOffset;
        double yFactor = startY / yOffset;

        // Idea is xFactor * xOffset = xStart

        if (xFactor > 1)
        {
            setxFactor(1.0);
        }
        else
        {
            setxFactor(xFactor);
        }

        if (yFactor > 1)
        {
            setyFactor(1.0);
        }
        else
        {
            setyFactor(startY / yOffset);
        }
    }

    public void TryToCenterToPostition(int x, int y)
    {
        setToStartPos(getCenteredStart(x, visibleCellsX, maxStartCellX), getCenteredStart(y, visibleCellsY, maxStartCellY));
    }

    private int getCenteredStart(int z, int visibleCellsZ, int maxStartCell)
    {
        int zStart;
        //Try to perfectly center the position
        if ((z - visibleCellsZ / 2) >= 0)
        {
            if ((z + visibleCellsZ / 2) <= mapSize)
            {
                zStart = z - visibleCellsZ / 2;
            }
            //Z would cut right side have to adjust
            else
            {
                zStart = maxStartCell;
            }
        }
        //Z would cut left side set to 0
        else
        {
            zStart = 0;
        }
        return zStart;
    }

    private void completeCalculation()
    {
        calcOffset();
        calculateVisibleCells();
        calculateStartPos();
        calculateMaxStartCells();
    }

    private void calculateMaxStartCells()
    {
        maxStartCellX = (int) Math.round(xOffset / CELL_SIZE);
        maxStartCellY = (int) Math.round(yOffset / CELL_SIZE);
    }

    private void FactorChanged(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal)
    {
        completeCalculation();
    }


    private void calcOffset()
    {
        xOffset = (mapSize * CELL_SIZE) - (getWidht() / zoomFactor.get());
        yOffset = (mapSize * CELL_SIZE) - (getHeight() / zoomFactor.get());
    }

    private void calculateVisibleCells()
    {
        visibleCellsX = (int) Math.round((width.get() / zoomFactor.get()) / CELL_SIZE);
        visibleCellsY = (int) Math.round((height.get() / zoomFactor.get()) / CELL_SIZE);
    }


    private void calculateStartPos()
    {
        int xCells = (int) Math.round((xOffset * getxFactor()) / CELL_SIZE);
        int yCells = (int) Math.round((yOffset * getyFactor()) / CELL_SIZE);

        xStartCell = xCells;
        yStartCell = yCells;

    }

    public int getVisibleCellsX()
    {
        return visibleCellsX;
    }

    public int getVisibleCellsY()
    {
        return visibleCellsY;
    }

    public double getHeight()
    {
        return height.get();
    }

    public double getWidht()
    {
        return width.get();
    }

    public double getZoomFactor()
    {
        return zoomFactor.get();
    }

    public void setZoomFactor(double zoomFactor)
    {
        this.zoomFactor.set(zoomFactor);
    }

    public SimpleDoubleProperty zoomFactorProperty()
    {
        return zoomFactor;
    }

    public int getxStartCell()
    {
        return xStartCell;
    }

    public int getyStartCell()
    {
        return yStartCell;
    }

    public double getxFactor()
    {
        return xFactor.get();
    }

    public void setxFactor(double xFactor)
    {
        this.xFactor.set(xFactor);
    }

    public SimpleDoubleProperty xFactorProperty()
    {
        return xFactor;
    }

    public double getyFactor()
    {
        return yFactor.get();
    }

    public void setyFactor(double yFactor)
    {
        this.yFactor.set(yFactor);
    }

    public SimpleDoubleProperty yFactorProperty()
    {
        return yFactor;
    }

    public int getMapSize()
    {
        return mapSize;
    }

    public double getxOffset()
    {
        return xOffset;
    }

    public double getyOffset()
    {
        return yOffset;
    }
}
