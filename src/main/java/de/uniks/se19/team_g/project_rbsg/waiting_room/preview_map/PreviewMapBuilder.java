package de.uniks.se19.team_g.project_rbsg.waiting_room.preview_map;

import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.lang.NonNull;

import java.util.List;

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
                .orElse(0);

        final int yGridSize = cells
                .stream()
                .mapToInt(Cell::getY)
                .max()
                .orElse(0);

        final double cellWidth = width / xGridSize;
        final double cellHeight = height / yGridSize;

        return canvas;
    }
}
