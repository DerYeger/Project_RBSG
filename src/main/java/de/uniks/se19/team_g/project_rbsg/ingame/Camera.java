package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.beans.property.SimpleIntegerProperty;

public class Camera
{
    private SimpleIntegerProperty xPos;
    private SimpleIntegerProperty yPos;
    private SimpleIntegerProperty viewSize;
    private final int mapSize;

    public Camera(int xPos, int yPos, int viewSize, int mapSize) {
        this.xPos.setValue(xPos);
        this.yPos.setValue(yPos);
        this.viewSize.setValue(viewSize);
        this.mapSize = mapSize;
    }

    public int getxPos()
    {
        return xPos.get();
    }

    public SimpleIntegerProperty xPosProperty()
    {
        return xPos;
    }

    public void setxPos(int xPos)
    {
        this.xPos.set(xPos);
    }

    public int getyPos()
    {
        return yPos.get();
    }

    public SimpleIntegerProperty yPosProperty()
    {
        return yPos;
    }

    public void setyPos(int yPos)
    {
        this.yPos.set(yPos);
    }

    public int getViewSize() {
        return viewSize.get();
    }

    public SimpleIntegerProperty viewSizeProperty() {
        return viewSize;
    }

    public void setViewSize(int viewSize)
    {
        this.viewSize.set(viewSize);
    }
}
