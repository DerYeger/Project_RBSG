package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Unit;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

/**
 * @author  Keanu Stückrad
 * @author  Georg Siebert
 */
@Scope("prototype")
@Controller
public class IngameViewController implements RootController {

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH/2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60)/2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);

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

    private int zoomFactor = 1;

    private TileDrawer tileDrawer;
    private SimpleObjectProperty<Tile> selectedTile;
    private SimpleObjectProperty<Tile> hoveredTile;

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
        this.tileDrawer = new TileDrawer();
        this.selectedTile = new SimpleObjectProperty<>(null);
        this.hoveredTile = new SimpleObjectProperty<>(null);
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
            cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells)
            {
                tileMap[cell.getY()][cell.getX()] = new Tile(cell);
            }

            for (Unit unit : units)
            {
                unit.getPosition().addListener(this::unitChangedPosition);
            }

            initCanvas();
        }


        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(this::unitListChanged);
        selectedTile.addListener(this::selectedTileChanged);
        hoveredTile.addListener(this::hoveredTileChanged);

        Unit unit = new Unit("Cool Id");
        Unit unitTwo = new Unit("Cooler Id");
        Unit unitThree = new Unit("Coolest Id");
        unit.setPosition(tileMap[0][0].getCell());
        unitTwo.setPosition(tileMap[1][4].getCell());
        unitThree.setPosition(tileMap[4][1].getCell());
        units.add(unit);
        units.add(unitTwo);
        units.add(unitThree);
        unit.getPosition().addListener(this::unitChangedPosition);
        unitTwo.getPosition().addListener(this::unitChangedPosition);
        unitThree.getPosition().addListener(this::unitChangedPosition);
        unit.setPosition(tileMap[3][3].getCell());
        unit.setPosition(tileMap[4][4].getCell());
        unit.setPosition(tileMap[5][5].getCell());

    }

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile == newTile) {
            return;
        }

        if(oldTile != null && oldTile.getHighlightingTwo() != HighlightingTwo.SELECTED) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
            tileDrawer.drawTile(oldTile);
        }

        if(newTile != null && newTile.getHighlightingTwo() != HighlightingTwo.SELECTED) {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
            tileDrawer.drawTile(newTile);
        }
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile == newTile) {
            newTile.setHighlightingTwo(HighlightingTwo.NONE);
            tileDrawer.drawTile(newTile);
            selectedTile.set(null);
        }

        if(oldTile != null) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
            tileDrawer.drawTile(oldTile);
        }

        if(newTile != null) {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
            tileDrawer.drawTile(newTile);
        }
    }

    private void unitListChanged(ListChangeListener.Change<? extends Unit> c)
    {
        logger.debug("Hello");
    }


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition)
    {
        tileDrawer.drawTile(tileMap[lastPosition.getY()][lastPosition.getX()]);
        tileDrawer.drawTile(tileMap[newPosition.getY()][newPosition.getX()]);
    }



    public void canvasHandleMouseMove(MouseEvent event) {
        int xPos = (int) (event.getX()/CELL_SIZE);
        int yPos = (int) (event.getY()/CELL_SIZE);
        hoveredTile.set(tileMap[yPos][xPos]);
    }

    public void canvasHandleMouseClicked(MouseEvent event) {
        if(!event.isDragDetect()) {
            int xPos = (int) (event.getX()/CELL_SIZE);
            int yPos = (int) (event.getY()/CELL_SIZE);
            selectedTile.set(tileMap[yPos][xPos]);
        }
    }

    private void initCanvas() {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        root.getChildren().add(zoomableScrollPane);
        canvas.setHeight(CELL_SIZE*mapSize);
        canvas.setWidth(CELL_SIZE*mapSize);

        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
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
