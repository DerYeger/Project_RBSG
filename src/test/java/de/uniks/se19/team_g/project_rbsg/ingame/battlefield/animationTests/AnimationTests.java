package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.animationTests;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.command.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.configuration.*;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.*;
import de.uniks.se19.team_g.project_rbsg.ingame.event.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.*;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.*;
import de.uniks.se19.team_g.project_rbsg.scene.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.stage.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.testfx.framework.junit.*;
import org.testfx.util.*;

import java.util.concurrent.*;

import static org.mockito.Mockito.*;

// Ignore because of the long running time
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
        LocaleConfig.class,
        AnimationTests.ContextConfiguration.class,
})
public class AnimationTests extends ApplicationTest
{
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
    private double battleFieldCenterX;
    private double battleFieldCenterY;
    private ViewComponent<BattleFieldController> battleFieldComponent;

    @Override
    public void start (@NonNull final Stage ignored)
    {
        battleFieldComponent = battleFieldFactory.getObject();
    }

    @Test
    public void testAnimations () throws ExecutionException, InterruptedException
    {
        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        de.uniks.se19.team_g.project_rbsg.ingame.model.Game game = definition.game;
        de.uniks.se19.team_g.project_rbsg.ingame.model.Unit playerUnit = definition.playerUnit;
        Cell[][] cells = definition.cells;

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

        IngameContext context = new IngameContext(
                new UserProvider().set(user),
                new GameProvider(),
                new IngameGameProvider()
        );
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

        Platform.runLater(() ->
                          {
                              playerUnit.setPosition(cells[3][3]);

                          });

        WaitForAsyncUtils.waitForFxEvents();

        sleep(2000);

        Platform.runLater(() ->
                          {
                              playerUnit.setPosition(null);

                          });

        WaitForAsyncUtils.waitForFxEvents();
        sleep(2000);

        Platform.runLater(() ->
                          {
                              playerUnit.setPosition(cells[0][0]);

                          });

        WaitForAsyncUtils.waitForFxEvents();
        sleep(2000);

        Platform.runLater(() ->
                          {
                              playerUnit.setHp(1);

                          });

        WaitForAsyncUtils.waitForFxEvents();
        sleep(2000);


        Assert.assertNotNull(lookup("#animationButton").query());
        clickOn("#animationButton");

        Platform.runLater(() ->
                          {
                              playerUnit.setPosition(cells[3][3]);

                          });

        WaitForAsyncUtils.waitForFxEvents();

        sleep(500);

        Platform.runLater(() ->
                          {
                              playerUnit.setPosition(null);

                          });

        WaitForAsyncUtils.waitForFxEvents();
        sleep(500);

        Platform.runLater(() ->
                          {
                              playerUnit.setPosition(cells[0][0]);

                          });

        WaitForAsyncUtils.waitForFxEvents();
        sleep(500);

        Platform.runLater(() ->
                          {
                              playerUnit.setHp(1);

                          });

        WaitForAsyncUtils.waitForFxEvents();
        sleep(500);
    }

    protected void revealBattleField (IngameContext context) throws ExecutionException, InterruptedException
    {
        // doing it like this saves the call to WaitForAsyncUtils and ensures that exceptions
        // in Platform.runLater() will result in failing tests right away
        CompletableFuture<Void> aVoid = CompletableFuture.runAsync(
                () ->
                {
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

    @TestConfiguration
    static class ContextConfiguration
    {

        @Bean
        public GameProvider gameProvider ()
        {
            final de.uniks.se19.team_g.project_rbsg.model.Game defaultGame = new de.uniks.se19.team_g.project_rbsg.model.Game("id", "", 2, 1);
            final GameProvider gameProvider = new GameProvider();
            gameProvider.set(defaultGame);
            return gameProvider;
        }

    }
}
