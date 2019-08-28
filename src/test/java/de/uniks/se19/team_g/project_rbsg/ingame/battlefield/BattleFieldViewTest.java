package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.bots.UserScopeBeanFactoryPostProcessor;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingOne;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.robot.Motion;
import org.testfx.util.WaitForAsyncUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author  Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        BattleFieldController.class,
        IngameConfig.class,
        ChatBuilder.class,
        ChatController.class,
        ChatTabManager.class,
        UserProvider.class,
        ChatCommandManager.class,
        GameEventManager.class,
        LocaleConfig.class
})
public class BattleFieldViewTest extends ApplicationTest {


    private double battleFieldCenterX;
    private double battleFieldCenterY;
    private ViewComponent<BattleFieldController> battleFieldComponent;

    @SpyBean
    SceneManager sceneManager;

    @MockBean
    AlertBuilder alertBuilder;

    @MockBean
    MenuBuilder menuBuilder;

    @MockBean
    MovementManager movementManager;

    @MockBean
    MusicManager musicManager;

    @Autowired
    ObjectFactory<ViewComponent<BattleFieldController>> battleFieldFactory;


    @Override
    public void start(@NonNull final Stage ignored) {
        battleFieldComponent = battleFieldFactory.getObject();
    }

    @Test
    public void testBuildIngameView() throws IOException, ExecutionException, InterruptedException {

        IngameGameProvider ingameGameProvider= new IngameGameProvider();
        ingameGameProvider.set(buildComplexTestGame());


        Node ingameView = battleFieldComponent.getRoot();
        BattleFieldController controller = battleFieldComponent.getController();

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        IngameContext context = new IngameContext(
                new User().setName("Test"),
                new de.uniks.se19.team_g.project_rbsg.model.Game("test", 4)
        );
        Player player = new Player("123");
        player.getUnits().addAll(
                new Unit("_5d25be843129f1000129ffe1"), new Unit("_5d25be843129f1000129ffe1"),
                new Unit("_5d25be843129f1000129ffe1"), new Unit("_5d25be843129f1000129ffe1"),
                new Unit("_5d25be843129f1000129ffe1"), new Unit("_5d25be843129f1000129ffe1"),
                new Unit("_5d25be843129f1000129ffe1"), new Unit("_5d25be843129f1000129ffe1"),
                new Unit("_5d25be843129f1000129ffe1"), new Unit("_5d25be843129f1000129ffe1"));
        player.setColor("RED");
        player.setName("Test");
        Game gameState = ingameGameProvider.get();
        gameState.getPlayers().add(player);
        gameState.setCurrentPlayer(player);
        context.setGameEventManager(gameEventManager);
        context.gameInitialized(gameState);
        revealBattleField(context);

        Assert.assertNotNull(ingameView);
        Canvas canvas = lookup("#canvas").query();
        Assert.assertNotNull(canvas);
        Button menu = lookup("#menuButton").query();
        Assert.assertNotNull(menu);
        Button zoomOut = lookup("#zoomOutButton").query();
        Assert.assertNotNull(zoomOut);
        Button zoomIn = lookup("#zoomInButton").query();
        Assert.assertNotNull(zoomIn);
        for(int i = 0; i < 6; i++) Platform.runLater( () -> controller.zoomOut(null));
        for(int i = 0; i < 12; i++) Platform.runLater( () -> controller.zoomIn(null));
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#zoomOutButton");

        Button endPhaseButton = lookup("#endPhaseButton").query();
        Assert.assertNotNull(endPhaseButton);

        Game game = gameState;
        Unit unit = new Unit("10");
        unit.setHp(10);
        unit.setMp(10);
        unit.setUnitType(UnitTypeInfo._CHOPPER);
        unit.setGame(game);
        unit.setPosition(game.getCells().get(11));
        unit.setLeader(player);

        game.getUnits().get(0).setPosition(gameState.getCells().get(12));

        click(25, 100);
        click(25, 150);
        click(75, 150);


        Button ingameInformationsButton = lookup("#playerButton").query();
        Assert.assertNotNull(ingameInformationsButton);
        HBox playerBar = lookup("#playerBar").query();
        clickOn("#playerButton");
        Assert.assertTrue(playerBar.isVisible());
        clickOn("#playerButton");
        Assert.assertFalse(playerBar.isVisible());
        Button chatButton = lookup("#chatButton").query();
        StackPane chatPane = lookup("#chatPane").query();
        clickOn("#chatButton");
        Assert.assertTrue(chatPane.isVisible());
        clickOn("#chatButton");
        Assert.assertFalse(chatPane.isVisible());
    }

    private void click(double x, double y) {
        clickOn(battleFieldCenterX + x, battleFieldCenterY + y, Motion.DIRECT);
    }

    @Test
    public void testMovement() throws ExecutionException, InterruptedException {
        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        IngameApi ingameApi = new IngameApi();
        GameEventManager gameEventManager = mock(GameEventManager.class);
        when(gameEventManager.api()).thenReturn(ingameApi);
        ingameApi.setGameEventManager(gameEventManager);

        User user = new User();
        user.setName("Karli");
        Player player = new Player("Karli").setName("Karli").setColor("RED");
        game.withPlayer(player);
        playerUnit.setLeader(player);
        definition.otherUnit.setLeader(player);
        game.setCurrentPlayer(null);

        playerUnit.setMp(3);
        playerUnit.setRemainingMovePoints(0);

        IngameContext context = new IngameContext(user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        CompletableFuture.runAsync(
                () -> context.getGameState().setPhase("movePhase"),
                Platform::runLater
        ).get();

        revealBattleField(context);
        CompletableFuture.runAsync(
            () -> game.setCurrentPlayer(player),
            Platform::runLater
        ).get();

        Assert.assertEquals(playerUnit.getMp(), playerUnit.getRemainingMovePoints());

        context.getGameState().setPhase("movePhase");
        // test unit selection
        Assert.assertNull(game.getSelectedUnit());
        click(-75, -75);
        Assert.assertNull(game.getSelectedUnit());
        click(-25, -75);
        Assert.assertSame(playerUnit, game.getSelectedUnit());

        //verifyZeroInteractions(movementManager);
        when(movementManager.getTour(playerUnit, definition.cells[1][0]))
                .thenReturn(null);

        // test unit selection removed if not reachable terrain is clicked
        click(-75, -25);
        //verify(movementManager).getTour(playerUnit, definition.cells[1][0]);
        Assert.assertNull(game.getSelectedUnit());
        click(-25, -75);
        Assert.assertSame(playerUnit, game.getSelectedUnit());

        //verifyNoMoreInteractions(movementManager);
        //verifyZeroInteractions(gameEventManager);

        Tour tour = new Tour();
        tour.setCost(2);
        tour.setPath(Collections.singletonList(definition.cells[0][0]));
        when(movementManager.getTour(playerUnit, definition.cells[0][0]))
                .thenReturn(tour);
        doAnswer(
                invocation -> {
                    CommandBuilder.MoveUnitData data = (CommandBuilder.MoveUnitData) ((Map) invocation.getArgument(0)).get("data");
                    Assert.assertEquals(data.unitId, playerUnit.getId());
                    Assert.assertEquals(
                            Collections.singletonList("0:0"),
                            data.path
                    );
                    return null;
                }
        ).when(gameEventManager).sendMessage(any());

        // test move action fired, if reachable terrain is clicked
        click(-75, -75);

        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertTrue(game.getInitiallyMoved());
        Assert.assertEquals(1, playerUnit.getRemainingMovePoints());
        Assert.assertSame(playerUnit, game.getSelectedUnit());

        //verify(movementManager, times(2)).getTour(any(), any());
        verify(gameEventManager).sendMessage(any());

        game.setCurrentPlayer(null);
        // test no action, if user is not current player
        click(-25, -25);
        //verifyNoMoreInteractions(gameEventManager);
        click(-25, -75);

    }

    @Test
    public void testButtonDisabled() throws ExecutionException, InterruptedException{

        BattleFieldController battleFieldController = battleFieldComponent.getController();

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        game.withPlayer(player);
        playerUnit.setLeader(player);
        game.setCurrentPlayer(player);

        definition.otherUnit.setLeader(player);

        IngameContext context = new IngameContext(user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");

        revealBattleField(context);

        Button endPhaseButton = lookup("#endPhaseButton").query();
        Assert.assertTrue(endPhaseButton.isDisabled());

        game.setInitiallyMoved(true);

        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertFalse(endPhaseButton.isDisabled());

    }

    @Test
    public void testUnitSelectionByKey() throws ExecutionException, InterruptedException {
        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        playerUnit.setLeader(player);

        Unit secondUnit = new Unit("second").setUnitType(UnitTypeInfo._JEEP);
        secondUnit.setLeader(player);
        secondUnit.setPosition(definition.cells[1][1]);
        game.withUnit(secondUnit);

        Unit thirddUnit = new Unit("third").setUnitType(UnitTypeInfo._JEEP);
        thirddUnit.setLeader(player);
        thirddUnit.setPosition(definition.cells[2][1]);
        game.withUnit(thirddUnit);

        game.withPlayer(player);
        playerUnit.setLeader(player);
        //otherUnit.setLeader(player);
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");
        revealBattleField(context);
        WaitForAsyncUtils.waitForFxEvents();
        Platform.runLater(() -> game.setSelectedUnit(playerUnit));
        WaitForAsyncUtils.waitForFxEvents();
        press(KeyCode.E);
        release(KeyCode.E);
        Assert.assertSame(secondUnit, game.getSelectedUnit());

        press(KeyCode.E);
        release(KeyCode.E);
        Assert.assertSame(thirddUnit, game.getSelectedUnit());

        press(KeyCode.Q);
        release(KeyCode.Q);
        press(KeyCode.Q);
        release(KeyCode.Q);
        Assert.assertSame(playerUnit, game.getSelectedUnit());

        secondUnit.setLeader(null);
        press(KeyCode.E);
        release(KeyCode.E);
        Assert.assertSame(thirddUnit, game.getSelectedUnit());
    }

    @Test
    public void testMovementArea() throws ExecutionException, InterruptedException{

        BattleFieldController battleFieldController = battleFieldComponent.getController();

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        game.withPlayer(player);
        playerUnit.setLeader(player);
        definition.otherUnit.setLeader(player);
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");
        revealBattleField(context);
        context.getGameState().getCells().get(5).setUnit(null);
        context.getGameState().setPhase("movePhase");
        WaitForAsyncUtils.waitForFxEvents();

        Tour tour = new Tour();

        when(movementManager.getTour(playerUnit, definition.cells[0][0])).thenReturn(tour);

        click(-20, -70);
        Assert.assertSame(playerUnit, game.getSelectedUnit());

        Cell cell = definition.cells[0][0];
        Cell target = definition.cells[1][1];

        Assert.assertTrue(cell.isIsReachable());
        Assert.assertEquals(HighlightingOne.MOVE, cell.getTile().getHighlightingOne());

        click(-20, -70);
        Assert.assertNull(game.getSelectedUnit());

        Assert.assertFalse(cell.isIsReachable());
        Assert.assertEquals(HighlightingOne.NONE, cell.getTile().getHighlightingOne());

        click(-20, -70);
        Assert.assertSame(playerUnit, game.getSelectedUnit());

        Assert.assertTrue(cell.isIsReachable());
        Assert.assertEquals(HighlightingOne.MOVE, cell.getTile().getHighlightingOne());

        when(movementManager.getTour(playerUnit, definition.cells[0][0])).thenReturn(null);
        playerUnit.setRemainingMovePoints(2);
        playerUnit.setPosition(target);
    }

    @Test
    // actually not his responsibility anymore
    public void testAttackRadius() throws ExecutionException, InterruptedException{
        BattleFieldController battleFieldController = battleFieldComponent.getController();

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;
        playerUnit.setPosition(playerUnit.getPosition().getBottom());

        Unit enemyUnit = new Unit("1");
        enemyUnit.setUnitType(UnitTypeInfo._BAZOOKA_TROOPER);
        enemyUnit.setPosition(playerUnit.getPosition().getRight());

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("BLUE");

        User enemy = new User();
        enemy.setName("Dinkelberg");
        Player enenymPlayer = new Player("Dinkelberg").setName("Dinkelberg").setColor("RED");

        game.withPlayer(player);
        game.withPlayer(enenymPlayer);
        playerUnit.setLeader(player);
        enemyUnit.setLeader(enenymPlayer);
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");
        revealBattleField(context);
        Tile unitTile = playerUnit.getPosition().getTile();

        context.getGameState().setPhase("attackPhase");
        while(context.getGameState().getPhase()==null){
            sleep(1);
        }
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertNotEquals(HighlightingOne.ATTACK, unitTile.getHighlightingOne());

        click(-25, -25);
        Assert.assertSame(playerUnit, game.getSelectedUnit());
        Assert.assertNotEquals(HighlightingOne.ATTACK, unitTile.getHighlightingOne());

        context.getGameState().setPhase("movePhase");
        while(!context.getGameState().getPhase().equals("movePhase")){
            sleep(1);
        }
        WaitForAsyncUtils.waitForFxEvents();

        context.getGameState().setPhase("movePhase");
        Assert.assertNotEquals(HighlightingOne.ATTACK, unitTile.getHighlightingOne());
    }

    @Test
    public void testAttack() throws ExecutionException, InterruptedException {
        BattleFieldController battleFieldController = battleFieldComponent.getController();

        IngameApi ingameApi = mock(IngameApi.class);
        GameEventManager gameEventManager = mock(GameEventManager.class);
        when(gameEventManager.api()).thenReturn(ingameApi);

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAttack();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        Player other = new Player("other").setColor("BLUE");
        game.withPlayers(player, other);
        playerUnit.setLeader(player);
        definition.otherUnit.setLeader(other);
        playerUnit.setCanAttack(Collections.singleton(definition.otherUnit.getUnitType()));
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");
        revealBattleField(context);

        game.setPhase(Game.Phase.attackPhase.name());
        click(-25, -25);
        click( -25, 25);
        click(-25, -25);
        game.setPhase(Game.Phase.movePhase.name());
        //verifyZeroInteractions(gameEventManager);
        click(25, -25);
        game.setPhase(Game.Phase.attackPhase.name());

        click(-25, -25);
        click(25, -25);
        verify(gameEventManager, times(1)).api();
        //verifyNoMoreInteractions(gameEventManager);
        verify(ingameApi).attack(definition.playerUnit, definition.otherUnit);

        Assert.assertNull(game.getSelectedUnit());
    }

    @Test
    public void testGameWon(){

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        Player enemy = new Player("Karl").setName("Karl").setColor("BLUE");
        game.withPlayer(player).withPlayer(enemy);
        playerUnit.setLeader(player);
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");

        battleFieldComponent.getController().configure(context);

        context.getGameState().setWinner(player);

        verify(alertBuilder).priorityInformation(
                eq(AlertBuilder.Text.GAME_WON),
                any());
    }

    @Test
    public void testGameLost(){

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        Player enemy = new Player("Karl").setName("Karl").setColor("BLUE");
        game.withPlayer(player).withPlayer(enemy);
        playerUnit.setLeader(player);
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");

        battleFieldComponent.getController().configure(context);

        context.getGameState().setWinner(enemy);

        verify(alertBuilder).priorityInformation(
                eq(AlertBuilder.Text.GAME_SOMEBODY_ELSE_WON),
                any(),
                eq(enemy.getName()));
    }

    @Test
    public void lostAndChooseSpectate() {

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;
        Unit playerUnit = definition.playerUnit;
        Unit enemyUnit = definition.otherUnit;
        Unit thirdUnit = new Unit("LucyCat").setUnitType(UnitTypeInfo._JEEP);
        thirdUnit.setPosition(definition.cells[2][1]);

        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        Player enemy = new Player("Karl").setName("Karl").setColor("BLUE");
        Player thirdParty = new Player("Lucy").setName("Lucy").setColor("GREEN");
        game.withPlayer(player).withPlayer(enemy).withPlayer(thirdParty);
        playerUnit.setLeader(player);
        enemyUnit.setLeader(enemy);
        thirdUnit.setLeader(thirdParty);
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                user, null);
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        context.getUser().setName("Bob");

        //revealBattleField(context);

        battleFieldComponent.getController().configure(context);
        player.getUnits().removeAll(playerUnit);

        verify(alertBuilder).priorityConfirmation(
                eq(AlertBuilder.Text.GAME_LOST),
                any(),
                any());
    }



    protected void revealBattleField(IngameContext context) throws ExecutionException, InterruptedException {
        // doing it like this saves the call to WaitForAsyncUtils and ensures that exceptions
        // in Platform.runLater() will result in failing tests right away
        CompletableFuture<Void> aVoid = CompletableFuture.runAsync(
                () -> {
                    battleFieldComponent.getController().configure(context);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(battleFieldComponent.getRoot()));
                    stage.centerOnScreen();
                    stage.show();
                },
                Platform::runLater
        );
        aVoid.get();

        WaitForAsyncUtils.waitForFxEvents();

        Node battleField = lookup("#battleFieldViewer").query();
        Bounds bounds = battleField.localToScreen(battleField.getBoundsInLocal());
        battleFieldCenterX = (bounds.getMaxX() + bounds.getMinX()) * 0.5;
        battleFieldCenterY = (bounds.getMaxY() + bounds.getMinY()) * 0.5;

    }

    public static Game buildComplexTestGame() throws IOException {
        Game game = new Game("AmazingGame24");
        BufferedReader in;

        String path = "game.txt";
        in = new BufferedReader(new FileReader(new File(path)));
        String zeile;
        while ((zeile = in.readLine()) != null) {
            int idAnf = zeile.indexOf('@')+1;
            int idEnd = zeile.indexOf('\"', idAnf);
            int xAnf = zeile.indexOf("\"x\":")+4;
            int xEnd = zeile.indexOf(",\"y");
            int yAnf = zeile.indexOf("\"y\":")+4;
            int yEnd = zeile.indexOf(",\"isPas");
            int bAnf = 7;
            int bEnd = zeile.indexOf('@');
            String biome = zeile.substring( bAnf, bEnd);
            Biome b = biome.equals("Grass") ? Biome.GRASS :
                    biome.equals("Forest") ? Biome.FOREST :
                            biome.equals("Mountain") ? Biome.MOUNTAIN :
                                    biome.equals("Water") ? Biome.WATER : null;
            Objects.requireNonNull(b);
            game.withCell(new Cell(zeile.substring( idAnf, idEnd))
                    .setX(Integer.parseInt(zeile.substring( xAnf, xEnd)))
                    .setY(Integer.parseInt(zeile.substring( yAnf, yEnd)))
                    .setBiome(b));
        }
        in = new BufferedReader(new FileReader(new File(path)));
        while((zeile = in.readLine()) != null){
            int idAnf = zeile.indexOf('@')+1;
            int idEnd = idAnf+8;
            int leAnf = zeile.indexOf('@', zeile.indexOf("\"left\""))+1;
            int leEnd = zeile.indexOf('\"', leAnf);
            int riAnf = zeile.indexOf('@', zeile.indexOf("\"right\""))+1;
            int riEnd = zeile.indexOf('\"', riAnf);
            int boAnf = zeile.indexOf('@', zeile.indexOf("\"bottom\""))+1;
            int boEnd = zeile.indexOf('\"', boAnf);
            int toAnf = zeile.indexOf('@', zeile.indexOf("\"top\""))+1;
            int toEnd = zeile.indexOf('\"', toAnf);
            for(Cell c: game.getCells()) {
                if(c.getId().equals(zeile.substring( idAnf, idEnd))) {
                    if(leAnf > 0){
                        for(Cell ce: game.getCells()){
                            if(ce.getId().equals(zeile.substring( leAnf, leEnd))) {
                                c.setLeft(ce);
                            }
                        }
                    }
                    if(riAnf > 0){
                        for(Cell ce: game.getCells()){
                            if(ce.getId().equals(zeile.substring( riAnf, riEnd))) {
                                c.setRight(ce);
                            }
                        }
                    }
                    if(boAnf > 0){
                        for(Cell ce: game.getCells()){
                            if(ce.getId().equals(zeile.substring( boAnf, boEnd))) {
                                c.setBottom(ce);
                            }
                        }
                    }
                    if(toAnf > 0){
                        for(Cell ce: game.getCells()){
                            if(ce.getId().equals(zeile.substring( toAnf, toEnd))) {
                                c.setTop(ce);
                            }
                        }
                    }
                }
            }
        }

        in.close();


        for (int i = 0; i < 10; i++)
        {
            Unit unit = new Unit(String.valueOf(i));
            unit.setGame(game);
            unit.setPosition(game.getCells().get(i));
            unit.setHp(10);
            unit.setMp(10);
            unit.setUnitType(UnitTypeInfo._CHOPPER);
        }

        return game;
    }
}
