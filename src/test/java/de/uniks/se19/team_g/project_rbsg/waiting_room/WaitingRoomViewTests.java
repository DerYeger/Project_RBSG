package de.uniks.se19.team_g.project_rbsg.waiting_room;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.waiting_room.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Player;
import de.uniks.se19.team_g.project_rbsg.waiting_room.preview_map.PreviewMapBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

/**
 * @author  Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        WaitingRoomSceneBuilder.class,
        SplashImageBuilder.class,
        WaitingRoomViewBuilder.class,
        WaitingRoomViewController.class,
        WaitingRoomViewTests.ContextConfiguration.class,
        FXMLLoaderFactory.class,
        MusicManager.class,
        ChatBuilder.class,
        PreviewMapBuilder.class,
        IngameGameProvider.class
})
public class WaitingRoomViewTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader()
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }
        @Bean
        public GameProvider gameProvider() {
            return new GameProvider() {
                @Override
                public Game get() {
                    return new Game("id", "testGame", 4, 1);
                }
            };
        }

        @Bean
        public GameEventManager gameEventManager() {
            return new GameEventManager(new WebSocketClient()) {
                @Override
                public void startSocket(@NonNull final String gameID, @NonNull final String armyID) {
                    //do nothing
                }
            };
        }

        @Bean
        public ChatController chatController() {
            return new ChatController(new UserProvider(), new ChatCommandManager(), new ChatTabManager());
        }

        @Bean
        public ApplicationState applicationState() {
            ApplicationState applicationState = new ApplicationState();
            applicationState.selectedArmy.set(new Army());
            return applicationState;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }

        @Bean
        public SceneManager sceneManager() {
            return new SceneManager() {
                @Override
                public void setIngameScene(){

                }
                @Override
                public void setLobbyScene(@NonNull final boolean useCache, @Nullable final SceneIdentifier cacheIdentifier){

                }
            };
        }

        @Bean
        public ModelManager modelManager(){
            return new ModelManager() {
                @Override
                public de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game getGame(){
                    Player p1 = new Player("").setName("P1").setColor("BLACK");
                    Player p2 = new Player("").setName("P2").setColor("RED");
                    de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game game = new de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game("");
                    game.withPlayers(p1, p2);
                    return game;
                }
            };
        }

        @Bean
        public UserProvider userProvider(){
            return new UserProvider(){
                @Override
                public User get(){
                    return new User("P1",""){
                        @Override
                        public String getName(){
                            return "P1";
                        }
                    };
                }
            };
        }
    }

    @Autowired
    WaitingRoomSceneBuilder waitingRoomSceneBuilder;

    @Autowired
    ModelManager modelManager;


    private WaitingRoomViewBuilder waitingRoomViewBuilder;
    private Scene scene;

    @Override
    public void start(@NonNull final Stage stage) throws Exception {
        scene = waitingRoomSceneBuilder.getWaitingRoomScene();
        Assert.assertNotNull(scene);
        waitingRoomViewBuilder = context.getBean(WaitingRoomViewBuilder.class);
        Node waitingRoomNode = waitingRoomViewBuilder.buildWaitingRoomView();
        Assert.assertNotNull(waitingRoomNode);
        stage.setScene(new Scene((Parent) waitingRoomNode, 1200, 840));
        stage.show();
    }

    @Test
    public void testBuildPlayerCard() throws Exception {
        Label label = lookup("Waiting for\nplayer...").query();
        Assert.assertNotNull(label);
        waitingRoomViewBuilder.waitingRoomViewController.handle(null);
        Player p1 = new Player("123").setName("P1").setColor("GREEN");
        de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game game = new de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game("");
        game.withPlayer(p1);
        waitingRoomViewBuilder.waitingRoomViewController.setPlayerCards(game);
        Player p2 = new Player("456").setName("P2").setColor("BLUE");
        game.withPlayer(p2);
        Player p3 = new Player("123").setName("P3").setColor("YELLOW");
        game.withPlayer(p3);
        game.withoutPlayer(p2);
    }

    @Autowired
    MusicManager musicManager;

    @Test
    public void testButons() throws Exception {
        Button musicButton = lookup("#soundButton").query();
        Assert.assertNotNull(musicButton);
        clickOn("#soundButton");
        Assert.assertFalse(musicManager.musicRunning);
        clickOn("#soundButton");
        Assert.assertTrue(musicManager.musicRunning);
        Button infoButton = lookup("#showInfoButton").query();
        Assert.assertNotNull(infoButton);
        clickOn("#showInfoButton");
        Button leaveButton = lookup("#leaveButton").query();
        Assert.assertNotNull(leaveButton);
        clickOn("#leaveButton");
        clickOn("OK");
    }

}
