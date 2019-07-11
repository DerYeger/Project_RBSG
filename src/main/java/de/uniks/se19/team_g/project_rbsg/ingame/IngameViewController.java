package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.TileHighlighting;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Unit;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.UnitType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import static de.uniks.se19.team_g.project_rbsg.waiting_room.model.UnitType.*;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class IngameViewController implements RootController {

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH/2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60)/2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);
    private double columnRowSize;
    private double canvasColumnRowSize;

    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;
    public VBox root;

    private Canvas canvas;
    private ZoomableScrollPane zoomableScrollPane;
    private int mapSize;

    private Game game;
    private ObservableList<Cell> cells;
    private Tile[][] tileMap;
    private ObservableList<Unit> units;
    private GraphicsContext gc;

    private Image grass;
    private int zoomFactor = 1;

    private final IngameGameProvider ingameGameProvider;
    private final GameProvider gameProvider;
    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IngameViewController(@NonNull final IngameGameProvider ingameGameProvider,
                                @NonNull final GameProvider gameProvider,
                                @NonNull final SceneManager sceneManager,
                                @NonNull final AlertBuilder alertBuilder) {
        this.ingameGameProvider = ingameGameProvider;
        this.gameProvider = gameProvider;
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
    }

    public void initialize() {
        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                zoomInButton,
                getClass().getResource("/assets/icons/navigation/zoomInWhite.png"),
                getClass().getResource("/assets/icons/navigation/zoomInBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                zoomOutButton,
                getClass().getResource("/assets/icons/navigation/zoomOutWhite.png"),
                getClass().getResource("/assets/icons/navigation/zoomOutBlack.png"),
                40
        );
        game = ingameGameProvider.get();
        if(game == null) {
            // exception
        } else {

            mapSize = (int) Math.sqrt(cells.size());
            logger.debug("Actual map size:" + mapSize);
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell: cells)
            {
                tileMap[cell.getY()][cell.getX()] = new Tile(cell, TileHighlighting.NONE);
            }



            grass = new Image("/assets/cells/grass.png");
            cells = game.getCells();
            units = game.getUnits();
            columnRowSize = Math.sqrt(cells.size());
            canvasColumnRowSize = columnRowSize * CELL_SIZE;
            initCanvas();
        }


    }

    private void initCanvas() {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        root.getChildren().add(zoomableScrollPane);
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
            gc.drawImage(TileUtils.getBackgroundImage(cell), cell.getX() * CELL_SIZE, cell.getY() * CELL_SIZE);
        }
        String imagePath = "";
        for(Unit unit: units) {
            UnitType unitType = unit.getUnitType();
            if(unitType.equals(INFANTRY)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b6.getImage().toExternalForm();
            else if(unitType.equals(BAZOOKA_TROOPER)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b7.getImage().toExternalForm();
            else if(unitType.equals(JEEP)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b8.getImage().toExternalForm();
            else if(unitType.equals(LIGHT_TANK)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b9.getImage().toExternalForm();
            else if(unitType.equals(HEAVY_TANK)) imagePath = UnitTypeInfo._5cc051bd62083600017db3ba.getImage().toExternalForm();
            else if(unitType.equals(CHOPPER)) imagePath = UnitTypeInfo._5cc051bd62083600017db3bb.getImage().toExternalForm();
            else imagePath = UnitTypeInfo._5cc051bd62083600017db3b8.getImage().toExternalForm();
            gc.drawImage(
                    new Image(imagePath, CELL_SIZE, CELL_SIZE, false, true),
                    unit.getPosition().get().getX() * CELL_SIZE,
                    unit.getPosition().get().getY() * CELL_SIZE
            );
        }
    }






    public void leaveGame(ActionEvent actionEvent) {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.EXIT,
                        this::doLeaveGame,
                        null);
    }

    private void doLeaveGame() {
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
        gameProvider.clear();
        ingameGameProvider.clear();
    }

    public void zoomIn(ActionEvent actionEvent) {
        if(zoomFactor == 1) {
            zoomableScrollPane.onScroll(20.0, ZOOMPANE_CENTER);
            zoomFactor++;
        } else if(zoomFactor == 0) {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        } else if(zoomFactor == -1 && gameProvider.get().getNeededPlayer() == 4) {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        }
    }

    public void zoomOut(ActionEvent actionEvent) {
        if(zoomFactor == 2) {
            zoomableScrollPane.onScroll(-20.0, ZOOMPANE_CENTER);
            zoomFactor--;
        } else if(zoomFactor == 1) {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        } else if(zoomFactor == 0 && gameProvider.get().getNeededPlayer() == 4) {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        }
    }

}
