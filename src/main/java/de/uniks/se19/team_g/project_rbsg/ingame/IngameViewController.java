package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Biome;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class IngameViewController {

    private static final double CELL_SIZE = 64;
    private double columnRowSize;
    private double canvasColumnRowSize;

    public Canvas canvas;

    private Game game;
    private ObservableList<Cell> cells;
    private GraphicsContext gc;

    private Image grass;
    private Image forest;
    private Image water;
    private Image mountain;

    private final IngameGameProvider ingameGameProvider;

    @Autowired
    public IngameViewController(@NonNull final IngameGameProvider ingameGameProvider) {
        this.ingameGameProvider = ingameGameProvider;
    }

    public void init() {
        game = ingameGameProvider.get();
        if(game == null) {
            // exception
        } else {
            grass = new Image("/assets/cells/grass.png");
            forest = new Image("/assets/cells/forest.png");
            water = new Image("/assets/cells/water.png");
            mountain = new Image("/assets/cells/mountain.png");
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
            gc.drawImage(getBiome(cell.getBiome()), cell.getY() * CELL_SIZE, cell.getX() * CELL_SIZE);
        }
    }

    private Image getBiome(Biome biome) {
        switch (biome.toString()) {
            case "Mountain":
                return mountain;
            case "Water":
                return water;
            case "Forest":
                return forest;
            default:
                return grass;
        }
    }

}
