package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.alert.*;
import de.uniks.se19.team_g.project_rbsg.component.*;
import de.uniks.se19.team_g.project_rbsg.configuration.*;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import de.uniks.se19.team_g.project_rbsg.util.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

import javax.annotation.Nullable;
import javax.annotation.*;
import java.beans.*;

/**
 * @author Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController, Terminable
{

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH / 2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60) / 2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);
    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
    private final IngameGameProvider ingameProvider;
    private final ApplicationState appState;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChangeListener<Tile> hoveredTileListener = this::hoveredTileChanged;
    private final ChangeListener<Tile> selectedTileListener = this::selectedTileChanged;
    private final ListChangeListener<Unit> unitListListener = this::unitListChanged;
    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;
    public Button mapButton;
    public Canvas miniMap;
    public Button endPhaseButton;
    public Pane endPhaseButtonContainer;
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
    private final ChangeListener<Cell> unitPositionListener = this::unitChangedPosition;
    private final PropertyChangeListener highlightingListener = this::highlightingChanged;
    private SimpleObjectProperty<Tile> selectedTile;
    private SimpleObjectProperty<Tile> hoveredTile;
    private IngameContext context;

    @Autowired
    public BattleFieldController(
            @NonNull final SceneManager sceneManager,
            @NonNull final AlertBuilder alertBuilder,
            @Nullable final ApplicationState appState,
            @NonNull final IngameGameProvider ingameGameProvider
    )
    {
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.appState = appState;
        this.tileDrawer = new TileDrawer();
        this.ingameProvider = ingameGameProvider;
        this.selectedTile = new SimpleObjectProperty<>(null);
        this.hoveredTile = new SimpleObjectProperty<>(null);
    }

    public Tile getSelectedTile()
    {
        return selectedTile.get();
    }

    public SimpleObjectProperty<Tile> selectedTileProperty()
    {
        return selectedTile;
    }

    public Tile getHoveredTile()
    {
        return hoveredTile.get();
    }

    public SimpleObjectProperty<Tile> hoveredTileProperty()
    {
        return hoveredTile;
    }

    public void initialize()
    {

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

        JavaFXUtils.setButtonIcons(
                endPhaseButton,
                getClass().getResource("/assets/icons/operation/endPhaseWhite.png"),
                getClass().getResource("/assets/icons/operation/endPhaseBlack.png"),
                40
        );

        miniMap.setVisible(false);
        JavaFXUtils.setButtonIcons(
                mapButton,
                getClass().getResource("/assets/icons/operation/mapClosedBlack.png"),
                getClass().getResource("/assets/icons/operation/mapClosedWhite.png"),
                40);
    }

    private void highlightingChanged(PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if (oldTile != null && oldTile.getHighlightingTwo() != HighlightingTwo.SELECTED)
        {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if (newTile != null && newTile.getHighlightingTwo() != HighlightingTwo.SELECTED)
        {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
        }
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if (oldTile != null)
        {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if (newTile != null)
        {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
        }
    }

    private void unitListChanged(ListChangeListener.Change<? extends Unit> c)
    {
        while (c.next())
        {
            logger.debug(c.toString());
            if (c.wasAdded())
            {
                for (Unit unit : c.getAddedSubList())
                {
                    unit.getPosition().addListener(unitPositionListener);
                }
            }

            if (c.wasRemoved())
            {
                for (Unit unit : c.getRemoved())
                {
                    unit.getPosition().removeListener(unitPositionListener);
                }
            }
        }
    }


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition)
    {
        if (lastPosition != null)
        {
            tileDrawer.drawTile(tileMap[lastPosition.getY()][lastPosition.getX()]);
        }
        if (newPosition != null)
        {
            tileDrawer.drawTile(tileMap[newPosition.getY()][newPosition.getX()]);
        }
    }

    private void initCanvas()
    {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        root.getChildren().add(zoomableScrollPane);
        canvas.setHeight(CELL_SIZE * mapSize);
        canvas.setWidth(CELL_SIZE * mapSize);

        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
    }

    public void canvasHandleMouseMove(MouseEvent event)
    {
        int xPos = (int) (event.getX() / CELL_SIZE);
        int yPos = (int) (event.getY() / CELL_SIZE);
        hoveredTile.set(tileMap[yPos][xPos]);
    }

    public void canvasHandleMouseClicked(MouseEvent event)
    {
        int xPos = (int) (event.getX() / CELL_SIZE);
        int yPos = (int) (event.getY() / CELL_SIZE);
        if (tileMap[yPos][xPos].equals(selectedTile.get()))
        {
            selectedTile.set(null);
            hoveredTile.set(null);
        }
        else
        {
            selectedTile.set(tileMap[yPos][xPos]);
        }

    }

    public void leaveGame(ActionEvent actionEvent)
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.EXIT,
                        this::doLeaveGame,
                        null);
    }

    private void doLeaveGame()
    {
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
    }

    public void zoomIn(ActionEvent actionEvent)
    {
        if (zoomFactor == 1)
        {
            zoomableScrollPane.onScroll(20.0, ZOOMPANE_CENTER);
            zoomFactor++;
        }
        else if (zoomFactor == 0)
        {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        }
        else if (zoomFactor == -1 && context.getGameData().getNeededPlayer() == 4)
        {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        }
    }

    public void zoomOut(ActionEvent actionEvent)
    {
        if (zoomFactor == 2)
        {
            zoomableScrollPane.onScroll(-20.0, ZOOMPANE_CENTER);
            zoomFactor--;
        }
        else if (zoomFactor == 1)
        {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        }
        else if (zoomFactor == 0 && context.getGameData().getNeededPlayer() == 4)
        {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        }
    }

    public void endPhase()
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.END_PHASE,
                        () -> this.context.getGameEventManager().sendEndPhaseCommand(),
                        null);
    }

    public void toggleMap(ActionEvent actionEvent)
    {
        if (miniMap.visibleProperty().get())
        {
            miniMap.visibleProperty().set(false);
            JavaFXUtils.setButtonIcons(
                    mapButton,
                    getClass().getResource("/assets/icons/operation/mapClosedBlack.png"),
                    getClass().getResource("/assets/icons/operation/mapClosedWhite.png"),
                    40);
        }
        else
        {
            miniMap.visibleProperty().set(true);
            JavaFXUtils.setButtonIcons(
                    mapButton,
                    getClass().getResource("/assets/icons/operation/mapBlack.png"),
                    getClass().getResource("/assets/icons/operation/mapWhite.png"),
                    40);
        }


    }

    @Override
    public void configure(@Nonnull IngameContext context)
    {

        this.context = context;

        game = context.getGameState();
        if (game == null)
        {
            // exception
        }
        else
        {
            cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells)
            {
                tileMap[cell.getY()][cell.getX()] = new Tile(cell);
                tileMap[cell.getY()][cell.getX()].addListener(highlightingListener);
            }

            for (Unit unit : units)
            {
                //Adds listener for units which are already in the list
                unit.getPosition().addListener(unitPositionListener);
            }

            initCanvas();
        }

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(unitListListener);
        selectedTile.addListener(selectedTileListener);
        hoveredTile.addListener(hoveredTileListener);

        BooleanProperty playerCanEndPhase = new SimpleBooleanProperty();

        ObjectProperty<Player> currentPlayerProperty = context.getGameState().currentPlayerProperty();

        playerCanEndPhase.bind(Bindings.createBooleanBinding(
                () -> {
                    boolean active = context.getUser().getName().equals(currentPlayerProperty.getName());

                    return (active && context.getGameState().initiallyMovedProperty().get());
                },
                currentPlayerProperty, context.getGameState().initiallyMovedProperty()
        ));

        context.getGameState().initiallyMovedProperty().bind(Bindings.createBooleanBinding(
                () -> false,
                currentPlayerProperty
        ));

        endPhaseButton.disableProperty().bind(playerCanEndPhase.not());
    }

    @Override
    public void terminate()
    {
        selectedTile.removeListener(selectedTileListener);
        hoveredTile.removeListener(hoveredTileListener);
        for (Tile[] tileArray : tileMap)
        {
            for (Tile tile : tileArray)
            {
                tile.removeListener(highlightingListener);
            }
        }

        for (Unit unit : units)
        {
            unit.getPosition().removeListener(unitPositionListener);
        }

        units.removeListener(unitListListener);
    }
}
