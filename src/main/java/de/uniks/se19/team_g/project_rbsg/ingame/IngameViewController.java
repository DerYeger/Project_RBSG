package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.BiomUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.MountainUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.WaterUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.ForestUrls;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class IngameViewController implements RootController {

    private static final double CELL_SIZE = 64;
    private double columnRowSize;
    private double canvasColumnRowSize;

    public Canvas canvas;

    private Game game;
    private ObservableList<Cell> cells;
    private GraphicsContext gc;

    private Image grass;

    private final IngameGameProvider ingameGameProvider;

    @Autowired
    public IngameViewController(@NonNull final IngameGameProvider ingameGameProvider) {
        this.ingameGameProvider = ingameGameProvider;
    }

    public void initialize() {
        game = ingameGameProvider.get();
        if(game == null) {
            // exception
        } else {
            grass = new Image("/assets/cells/grass.png");
            cells = game.getCells();
            columnRowSize = Math.sqrt(cells.size());
            canvasColumnRowSize = columnRowSize * CELL_SIZE;
            initCanvas();
        }
    }

    private void initCanvas() {
        System.out.println(columnRowSize); // debugging
        canvas.setHeight(canvasColumnRowSize);
        canvas.setWidth(canvasColumnRowSize);
        gc = canvas.getGraphicsContext2D();
        for (int row = 0; row < canvasColumnRowSize; row += CELL_SIZE) {
            for (int column = 0; column < canvasColumnRowSize; column += CELL_SIZE) {
                gc.drawImage(grass , row, column);
            }
        }
        for(Cell cell: cells){
            if (cell.getBiome().toString().equals("Grass")){
                continue;
            }
            gc.drawImage(getImage(cell), cell.getX() * CELL_SIZE, cell.getY() * CELL_SIZE);
        }
    }

    private Image getImage(Cell cell) {
        String biome = cell.getBiome().toString().equals("Forest") ? "forest" :
                cell.getBiome().toString().equals("Water") ? "water" : "mountain";
        int neighborSize = countNeighbors(cell);
        switch (neighborSize) {
            case 0:
                return getImageNeighborsZero(cell, biome);
            case 1:
                return getImageNeighborOne(cell, biome);
            case 2:
                return getImageNeighborsTwo(cell, biome);
            case 3:
                return getImageNeighborsThree(cell, biome);
            case 4:
                return getImageNeighborsFour(cell, biome);
            case 5:
                return getImageNeighborsFive(cell, biome);
            case 6:
                return getImageNeighborsSix(cell, biome);
            case 7:
                return getImageNeighborsSeven(cell, biome);
            case 8:
                return getImageNeighborsEight(cell, biome);
            default:
                return grass;
        }
    }

    private Image getImageNeighborsEight(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getEight() :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getEight() : MountainUrls.getEight();
        return new Image("/assets/cells/" + biome + "/" + png);
    }

    private Image getImageNeighborsSeven(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getSeven(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getSeven(cell) : MountainUrls.getSeven(cell);
        return new Image("/assets/cells/" + biome + "/seven_neighbors/" + png);
    }

    private Image getImageNeighborsSix(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getSix(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getSix(cell) : MountainUrls.getSix(cell);
        return new Image("/assets/cells/" + biome + "/six_neighbors/" + png);
    }

    private Image getImageNeighborsFive(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getFive(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getFive(cell) : MountainUrls.getFive(cell);
        return new Image("/assets/cells/" + biome + "/five_neighbors/" + png);
    }

    private Image getImageNeighborsFour(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getFour(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getFour(cell) : MountainUrls.getFour(cell);
        return new Image("/assets/cells/" + biome + "/four_neighbors/" + png);
    }

    private Image getImageNeighborsThree(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getThree(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getThree(cell) : MountainUrls.getThree(cell);
        return new Image("/assets/cells/" + biome + "/three_neighbors/" + png);
    }

    private Image getImageNeighborsTwo(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getTwo(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getTwo(cell) : MountainUrls.getTwo(cell);
        return new Image("/assets/cells/" + biome + "/two_neighbors/" + png);
    }

    private Image getImageNeighborOne(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getOne(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getOne(cell) : MountainUrls.getOne(cell);
        return new Image("/assets/cells/" + biome + "/one_neighbor/" + png);
    }

    private Image getImageNeighborsZero(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getZero() :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getZero() : MountainUrls.getZero();
        return new Image("/assets/cells/" + biome + "/" + png);
    }

    private int countNeighbors(Cell cell) {
        String biome = cell.getBiome().toString();
        boolean isBiomeLeft = biome.equals(BiomUrls.getLeftBiom(cell));
        boolean isBiomeRight = biome.equals(BiomUrls.getRightBiom(cell));
        boolean isBiomeTop = biome.equals(BiomUrls.getTopBiom(cell));
        boolean isBiomeBottom = biome.equals(BiomUrls.getBottomBiom(cell));
        int size = isBiomeLeft ? 1 : 0;
        size += isBiomeRight ? 1 : 0;
        size += isBiomeTop ? 1 : 0;
        size += isBiomeBottom ? 1 : 0;
        if(isBiomeLeft && isBiomeBottom) {
            size += biome.equals(BiomUrls.getBottomLeftBiom(cell)) ? 1 : 0;
        }
        if(isBiomeRight && isBiomeBottom) {
            size += biome.equals(BiomUrls.getBottomRightBiom(cell)) ? 1 : 0;
        }
        if(isBiomeLeft && isBiomeTop) {
            size += biome.equals(BiomUrls.getTopLeftBiom(cell)) ? 1 : 0;
        }
        if(isBiomeRight && isBiomeTop) {
            size += biome.equals(BiomUrls.getTopRightBiom(cell)) ? 1 : 0;
        }
        return size;
    }
}
