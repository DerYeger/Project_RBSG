package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import javafx.beans.property.*;
import org.junit.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CameraTest
{
    @Test
    public void testCamera() {
        SimpleDoubleProperty zoomFactor = new SimpleDoubleProperty(0.7);
        DoubleProperty hValue = new SimpleDoubleProperty(0);
        DoubleProperty vValue = new SimpleDoubleProperty(0);
        ReadOnlyDoubleProperty height = new SimpleDoubleProperty(1000);
        ReadOnlyDoubleProperty width = new SimpleDoubleProperty(1000);
        int mapSize = 64;
        int CELL_SIZE = 64;

        Camera camera = new Camera(zoomFactor, hValue, vValue, mapSize, height, width);

        double offsetX = (mapSize*CELL_SIZE) - (width.get() /0.7);
        double offsetY = (mapSize*CELL_SIZE) - (width.get() /0.7);

        assertThat(camera.getxStartCell(), is(0));
        assertThat(camera.getyStartCell(), is(0));
        assertThat(camera.getVisibleCellsX(), is(22));
        assertThat(camera.getVisibleCellsY(), is(22));
        assertThat(camera.getxOffset(), is(offsetX));
        assertThat(camera.getyOffset(), is(offsetY));
        assertThat(camera.getMaxStartCellX(), is((int) Math.round(offsetX / CELL_SIZE)));
        assertThat(camera.getMaxStartCellY(), is((int) Math.round(offsetY / CELL_SIZE)));

        camera.TryToCenterToPostition(1, 1);

        assertThat(hValue.get(), is(0.0));
        assertThat(vValue.get(), is(0.0));

        assertThat(camera.getxStartCell(), is(0));
        assertThat(camera.getyStartCell(), is(0));

        camera.TryToCenterToPostition(64, 64);

        assertThat(hValue.get(), is(1.0));
        assertThat(vValue.get(), is(1.0));

        assertThat(camera.getxStartCell(), is(42));
        assertThat(camera.getyStartCell(), is(42));

        camera.TryToCenterToPostition(32, 64);

        assertThat(camera.getxStartCell(), is(21));
        assertThat(camera.getyStartCell(), is(42));

        camera.TryToCenterToPostition(32, 32);

        assertThat(camera.getxStartCell(), is(21));
        assertThat(camera.getyStartCell(), is(21));

        camera.TryToCenterToPostition(0,11);

        assertThat(camera.getxStartCell(), is(0));
        assertThat(camera.getyStartCell(), is(0));
    }
}
