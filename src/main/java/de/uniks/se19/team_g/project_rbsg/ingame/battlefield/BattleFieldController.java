package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;

import java.beans.*;
import java.util.ArrayList;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController, Terminable
{

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

    @FXML
    public Button ingameInformationsButton;
    @FXML
    public HBox playerBar;
    @FXML
    public Pane player1;
    @FXML
    public Pane player2;
    @FXML
    public Pane player3;
    @FXML
    public Pane player4;
    @FXML
    public StackPane ingameField;

    private PlayerListController playerListController;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public BattleFieldController(
            @NonNull final IngameGameProvider ingameGameProvider,
            @NonNull final GameProvider gameProvider,
            @NonNull final SceneManager sceneManager,
            @NonNull final AlertBuilder alertBuilder
    ) {
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
        JavaFXUtils.setButtonIcons(
                ingameInformationsButton,
                getClass().getResource("/assets/icons/operation/accountWhite.png"),
                getClass().getResource("/assets/icons/operation/accountBlack.png"),
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
                tileMap[cell.getY()][cell.getX()].addListener(this::highlightingChanged);
            }

            for (Unit unit : units)
            {
                //Adds listener for units which are already in the list
                unit.getPosition().addListener(this::unitChangedPosition);
            }

            initCanvas();
        }

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        ArrayList<Pane> playerCardList = new ArrayList<Pane>();
        playerCardList.add(player1);
        playerCardList.add(player2);
        playerCardList.add(player3);
        playerCardList.add(player4);

        playerListController=new PlayerListController(this.game);
        int counter=0;
        for(Player player : this.game.getPlayers()){
            playerCardList.get(counter).getChildren().add(playerListController.getPlayerCards().get(counter));
            counter++;
        }

        //Listener for unit list
        units.addListener(this::unitListChanged);
        selectedTile.addListener(this::selectedTileChanged);
        hoveredTile.addListener(this::hoveredTileChanged);
        playerListController=new PlayerListController(game);
        playerBar.setVisible(false);
    }

    private void highlightingChanged(PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile != null && oldTile.getHighlightingTwo() != HighlightingTwo.SELECTED) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if(newTile != null && newTile.getHighlightingTwo() != HighlightingTwo.SELECTED) {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
        }
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile != null) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if(newTile != null) {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
        }
    }

    private void unitListChanged(ListChangeListener.Change<? extends Unit> c)
    {
        if (c.next()) {
            logger.debug(c.toString());
            if (c.wasAdded()) {
                for (int i = c.getFrom(); i < c.getTo(); i++)
                {
                    units.get(c.getFrom()).getPosition().addListener(this::unitChangedPosition);
                }
            }

            if(c.wasRemoved()) {
                for (Unit unit : c.getRemoved())
                {
                    unit.getPosition().removeListener(this::unitChangedPosition);
                }
            }
        }
    }


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition)
    {
        if(lastPosition != null) {
            tileDrawer.drawTile(tileMap[lastPosition.getY()][lastPosition.getX()]);
        }
        if(newPosition != null) {
            tileDrawer.drawTile(tileMap[newPosition.getY()][newPosition.getX()]);
        }
    }

    private void initCanvas() {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        ingameField.getChildren().add(zoomableScrollPane);
        canvas.setHeight(CELL_SIZE*mapSize);
        canvas.setWidth(CELL_SIZE*mapSize);

        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
    }

    public void canvasHandleMouseMove(MouseEvent event) {
        int xPos = (int) (event.getX()/CELL_SIZE);
        int yPos = (int) (event.getY()/CELL_SIZE);
        hoveredTile.set(tileMap[yPos][xPos]);
    }

    public void canvasHandleMouseClicked(MouseEvent event) {
        int xPos = (int) (event.getX()/CELL_SIZE);
        int yPos = (int) (event.getY()/CELL_SIZE);
        if(tileMap[yPos][xPos].equals(selectedTile.get())) {
            selectedTile.set(null);
            hoveredTile.set(null);
        }
        else{
            selectedTile.set(tileMap[yPos][xPos]);
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

    @Override
    public void configure(@Nonnull IngameContext context) {

    }

    @Override
    public void terminate()
    {
        selectedTile.removeListener(this::selectedTileChanged);
        hoveredTile.removeListener(this::hoveredTileChanged);
        for (Tile[] tileArray : tileMap)
        {
            for (Tile tile : tileArray)
            {
                tile.removeListener(this::highlightingChanged);
            }
        }

        for (Unit unit : units)
        {
            unit.getPosition().removeListener(this::unitChangedPosition);
        }
    }
    public void openPlayerBar(@Nonnull final ActionEvent event){
        if(playerBar.visibleProperty().get()==false){
            playerBar.visibleProperty().setValue(true);
            playerBar.toFront();
        }else
        {
            playerBar.visibleProperty().setValue(false);
            playerBar.toBack();
        }
    }
}
