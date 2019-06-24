package de.uniks.se19.team_g.project_rbsg.waiting_room.preview_map;

import static org.junit.Assert.*;

import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Biome;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;

import java.util.Arrays;
import java.util.List;

public class PreviewMapBuilderTests {

    @Test
    public void testPreviewMapBuilder() {
        final PreviewMapBuilder builder = new PreviewMapBuilder();

        final Cell forest = new Cell("")
                .setBiome(Biome.FOREST)
                .setX(0)
                .setY(0);
        final Cell grass = new Cell("")
                .setBiome(Biome.GRASS)
                .setX(1)
                .setY(0);
        final Cell mountain = new Cell("")
                .setBiome(Biome.MOUNTAIN)
                .setX(0)
                .setY(1);
        final Cell water = new Cell("")
                .setBiome(Biome.WATER)
                .setX(1)
                .setY(1);

        final List<Cell> cells = Arrays.asList(forest, grass, mountain, water);
        final Node result = builder.buildPreviewMap(cells, 40, 80);

        assertTrue(result instanceof Canvas);

        final Canvas canvas = (Canvas) result;

        assertEquals(40, canvas.getWidth(), 0);
        assertEquals(80, canvas.getHeight(), 0);

        final GraphicsContext gc = canvas.getGraphicsContext2D();

       assertEquals(Paint.valueOf("DARKBLUE"), gc.getFill());
    }
}
