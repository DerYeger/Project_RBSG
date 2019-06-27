package de.uniks.se19.team_g.project_rbsg.waiting_room.preview_map;

import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jan Müller
 */
@Component
public class PreviewMapBuilder {

    public Node buildPreviewMap(@NonNull final List<Cell> cells, @NonNull final double width, @NonNull final double height) {
        final Canvas canvas = new Canvas();
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setWidth(width);
        canvas.setHeight(height);

        final int xGridSize = cells
                .stream()
                .mapToInt(Cell::getX)
                .max()
                .orElse(0) + 1;

        final int yGridSize = cells
                .stream()
                .mapToInt(Cell::getY)
                .max()
                .orElse(0) + 1;

        final double cellWidth = width / xGridSize;
        final double cellHeight = height / yGridSize;

        for (final Cell cell : cells) {
            switch (cell.getBiome()) {
                case FOREST:
                    gc.setFill(Paint.valueOf("DARKGREEN"));
                    break;
                case GRASS:
                    gc.setFill(Paint.valueOf("GREEN"));
                    break;
                case MOUNTAIN:
                    gc.setFill(Paint.valueOf("#654321"));
                    break;
                case WATER:
                default:
                    gc.setFill(Paint.valueOf("DARKBLUE"));
            }

            gc.fillRect(
                    cell.getX() * cellWidth,
                    cell.getY() * cellHeight,
                    cellWidth,
                    cellHeight
            );
        }

        return canvas;
    }
}
