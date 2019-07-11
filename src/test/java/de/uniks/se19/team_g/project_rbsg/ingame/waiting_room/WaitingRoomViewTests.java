package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.army_builder.army_selection.ArmySelectorController;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.AppStateConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_map.PreviewMapBuilder;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author  Keanu Stückrad
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SceneManager.class,
        SplashImageBuilder.class,
        WaitingRoomViewController.class,
        WaitingRoomViewTests.ContextConfiguration.class,
        FXMLLoaderFactory.class,
        IngameGameProvider.class,
        PreviewMapBuilder.class,
        ChatBuilder.class,
        IngameConfig.class,
        AlertBuilder.class,
        FXMLLoaderFactory.class,
        AppStateConfig.class
})
public class WaitingRoomViewTests extends ApplicationTest {

    @Autowired ObjectFactory<ViewComponent<WaitingRoomViewController>> waitingRoomScene;

    @MockBean ArmySelectorController armySelectorController;

    @MockBean GameEventManager gameEventManager;

    @Autowired ApplicationState appState;

    @SpyBean ModelManager modelManager;

    @Autowired GameProvider gameProvider;

    @Autowired UserProvider userProvider;


    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public Function<VBox, ArmySelectorController> armySelectorComponent(ArmySelectorController armySelectorController) {
            return vBox -> armySelectorController;
        }

        @Bean
        public MusicManager musicManager()
        {
            return Mockito.mock(MusicManager.class, Mockito.RETURNS_SELF);
        }

        @Bean
        public GameProvider gameProvider() {
            final Game defaultGame = new Game("id", "testGame", 2, 1);
            final GameProvider gameProvider = new GameProvider();
            gameProvider.set(defaultGame);
            return gameProvider;
        }

        @Bean
        public ChatController chatController() {
            return new ChatController(new UserProvider(), new ChatCommandManager(), new ChatTabManager());
        }

        @Bean
        public SceneManager sceneManager() {
            return new SceneManager() {

            };
        }

        @Bean
        public UserProvider userProvider(){
            final UserProvider userProvider = new UserProvider();
            final User defaultUser =  new User("P1", "");
            userProvider.set(defaultUser);
            return userProvider;
        }
    }

    private Scene scene;

    private ViewComponent<WaitingRoomViewController> viewComponent;

    @Test
    public void testBuildWaitingRoomView() {
        final Node waitingRoomView = waitingRoomScene.getObject().getRoot();
        Assert.assertNotNull(waitingRoomView);
    }
    
    public void testBuildPlayerCard() throws ExecutionException, InterruptedException {
        WaitForAsyncUtils.asyncFx(
                () -> {
                    final ViewComponent<WaitingRoomViewController> buffer = waitingRoomScene.getObject();
                    viewComponent = buffer;
                    scene = new Scene(buffer.getRoot(), 1200, 840);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                }
        ).get();


        Label label = lookup("Waiting for\nplayer...").query();
        Assert.assertNotNull(label);
        de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Game game = new de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Game("");
        Player p1 = new Player("123").setName("P1").setColor("GREEN");
        Player p2 = new Player("456").setName("P2").setColor("BLUE");
        game.withPlayers(p1, p2);
        viewComponent.getController().setPlayerCards(game);
        Player p3 = new Player("123").setName("P3").setColor("YELLOW");
        game.withPlayer(p3);
        game.withoutPlayer(p2);
        WaitForAsyncUtils.waitForFxEvents();
        Label label2 = lookup("P1").query();
        Assert.assertNotNull(label2);
    }

    @Autowired
    MusicManager musicManager;

    @Test
    public void testButons() throws Exception {
        WaitForAsyncUtils.asyncFx(
            () -> {
                final ViewComponent<WaitingRoomViewController> buffer = waitingRoomScene.getObject();
                viewComponent = buffer;
                scene = new Scene(buffer.getRoot(), 1200, 840);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        ).get();

        Button musicButton = lookup("#soundButton").query();
        Assert.assertNotNull(musicButton);
        clickOn("#soundButton");
        verify(musicManager, Mockito.times(1)).updateMusicButtonIcons(musicButton);
        clickOn("#soundButton");
        verify(musicManager, Mockito.times(2)).updateMusicButtonIcons(musicButton);
        Button infoButton = lookup("#showInfoButton").query();
        Assert.assertNotNull(infoButton);
        clickOn("#showInfoButton");
        Button leaveButton = lookup("#leaveButton").query();
        Assert.assertNotNull(leaveButton);
        clickOn("#leaveButton");
    }

    @Test
    public void testArmySelector()
    {
        AtomicReference<Property<Army>> selectedReference = new AtomicReference<>();
        doAnswer(invocation -> {
            selectedReference.set(invocation.getArgument(1));
            return null;
        }).when(armySelectorController).setSelection(any(), any());

        final WaitingRoomViewController controller = this.waitingRoomScene.getObject().getController();

        final IngameContext context = new IngameContext(userProvider, gameProvider, new IngameGameProvider());
        context.setGameEventManager(gameEventManager);
        controller.configure(context);

        // think about verifying correct filter as well
        verify(armySelectorController, times(1)).setSelection(any(), any());
        final Property<Army> selectedArmy = selectedReference.get();

        Army army = new Army();
        army.id.set("1");

        selectedArmy.setValue(army);

        InOrder inOrder = inOrder(gameEventManager);
        inOrder.verify(gameEventManager).sendMessage(eq(CommandBuilder.changeArmy(army)));
        inOrder.verify(gameEventManager).sendMessage(eq(CommandBuilder.readyToPlay()));
    }

    @Test
    public void testGameStart() throws IOException {

        final WaitingRoomViewController controller = this.waitingRoomScene.getObject().getController();

        final User currentUser = new User();
        final UserProvider userProvider = new UserProvider().set(currentUser);
        final Game gameData = new Game("1", 2);
        final GameProvider gameDataProvider = new GameProvider().set(gameData);

        IngameContext context = new IngameContext(userProvider, gameDataProvider, new IngameGameProvider());
        context.setGameEventManager(gameEventManager);
        controller.configure(context);
        clearInvocations(gameEventManager);


        final de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Game gameState = new de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Game("1");

        context.gameInitialized(gameState);
        final Player bob = new Player("bob");
        final Player alice = new Player("alice");
        gameState.withPlayers(alice, bob);

        verifyZeroInteractions(gameEventManager);
        bob.setIsReady(true);
        verifyZeroInteractions(gameEventManager);
        alice.setIsReady(true);
        verifyZeroInteractions(gameEventManager);
        alice.setIsReady(false);
        verifyZeroInteractions(gameEventManager);

        gameData.setCreator(currentUser);

        alice.setIsReady(true);
        verify(gameEventManager).sendMessage(eq(CommandBuilder.startGame()));
    }
}
