package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url.BiomUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url.ForestUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url.MountainUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url.WaterUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.model.UnitType;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.uniks.se19.team_g.project_rbsg.ingame.model.UnitType.*;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController {

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH/2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60)/2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);
    private double columnRowSize;
    private double canvasColumnRowSize;

    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;

    public Button endPhaseButton;
    public Pane endPhaseButtonContainer;

    public VBox root;

    private Canvas canvas;
    private ZoomableScrollPane zoomableScrollPane;

    private Game game;
    private ObservableList<Cell> cells;
    private ObservableList<Unit> units;
    private GraphicsContext gc;

    private Image grass;
    private int zoomFactor = 1;

    private final IngameGameProvider ingameGameProvider;
    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
    private IngameContext context;

    private final ApplicationState appState;

    @Autowired
    public BattleFieldController(
            @NonNull final IngameGameProvider ingameGameProvider,
            @NonNull final GameProvider gameProvider,
            @NonNull final SceneManager sceneManager,
            @NonNull final AlertBuilder alertBuilder,
            @NonNull final UserProvider userProvider,
            @Nullable final ApplicationState appState
    ) {
        this.ingameGameProvider = ingameGameProvider;
        this.gameProvider = gameProvider;
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.userProvider = userProvider;
        this.appState = appState;
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
                endPhaseButton,
                getClass().getResource("/assets/icons/operation/endPhaseWhite.png"),
                getClass().getResource("/assets/icons/operation/endPhaseBlack.png"),
                40
        );
        game = ingameGameProvider.get();
        if(game == null) {
            // exception
        } else {
            grass = new Image("/assets/cells/grass.png");
            cells = game.getCells();
            units = game.getUnits();
            columnRowSize = Math.sqrt(cells.size());
            canvasColumnRowSize = columnRowSize * CELL_SIZE;
            initCanvas();
        }
        /*
        if (userProvider.get().getName() != ingameGameProvider.get().getCurrentPlayer().getName()){
            JavaFXUtils.bindButtonDisableWithTooltip(
                    endPhaseButton,
                    endPhaseButtonContainer,
                    new SimpleStringProperty(Rincl.getResources(ProjectRbsgFXApplication.class).getString("ValidArmyRequired")),
                    appState.meIsCurrentPlayer
                    );


        }
        */

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
            gc.drawImage(getImage(cell), cell.getX() * CELL_SIZE, cell.getY() * CELL_SIZE);
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

    public void endPhase() {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.END_PHASE,
                        () -> this.context.getGameEventManager().sendEndPhaseCommand(),
                        null);
    }

    @Override
    public void configure(@Nonnull IngameContext context) {
        this.context = context;
    }
}
