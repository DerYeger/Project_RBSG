package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.alert.ConfirmationAlertController;
import de.uniks.se19.team_g.project_rbsg.alert.InfoAlertController;
import de.uniks.se19.team_g.project_rbsg.configuration.AlertConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

/**
 * @author  Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        IngameViewTests.ContextConfiguration.class,
        BattleFieldController.class,
        IngameConfig.class,
        IngameContext.class,
        GameEventManager.class,
        InfoAlertController.class,
        ConfirmationAlertController.class,
        AlertConfig.class,
        AlertBuilder.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IngameViewTests extends ApplicationTest implements ApplicationContextAware {  // TODO Online Test ? for better coverage

    private ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        public IngameGameProvider ingameGameProvider() {
            return new IngameGameProvider(){
                @Override
                public Game get(){
                    Game game = new Game("game");
                    BufferedReader in = null;
                    try {
                        String path = "game.txt";
                        in = new BufferedReader(new FileReader(new File(path)));
                        String zeile = null;
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (in != null)
                            try {
                                in.close();
                            } catch (IOException e) {
                            }
                    }
                    return game;
                }
            };
        }
        @Bean
        public SceneManager sceneManager(){
            return new SceneManager() {
//                @Override
//                public void setLobbyScene(@NonNull final boolean useCache, @Nullable final SceneIdentifier cacheIdentifier) {
//
//                }
            };
        }
        @Bean
        public GameProvider gameProvider() {
            return new GameProvider(){
                @Override
                public de.uniks.se19.team_g.project_rbsg.model.Game get(){
                    de.uniks.se19.team_g.project_rbsg.model.Game game = new de.uniks.se19.team_g.project_rbsg.model.Game("test", 4);
                    return game;
                }
            };
        }

        @Override
        public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
            this.context = context;
        }

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader()
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }
    }

    private Scene scene;

    private SceneManager sceneManager;

    @Override
    public void start(@NonNull final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        Rincl.setLocale(Locale.ENGLISH);
        sceneManager = context.getBean(SceneManager.class);
        sceneManager.init(stage);
        @SuppressWarnings("unchecked")
        final Scene buffer = new Scene(((ViewComponent<RootController>) context.getBean("battleFieldScene")).getRoot());
        scene = buffer;
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();
    }

    @Test
    public void testBuildIngameView() {

        Canvas canvas = lookup("#canvas").query();
        Assert.assertNotNull(canvas);
        Button leave = lookup("#leaveButton").query();
        Assert.assertNotNull(leave);
        clickOn("#leaveButton");

        WaitForAsyncUtils.waitForFxEvents();

        final Button cancel = lookup("#cancel").queryButton();
        assertNotNull(cancel);

        WaitForAsyncUtils.waitForFxEvents();
        clickOn(cancel);
        WaitForAsyncUtils.waitForFxEvents();

        Button zoomOut = lookup("#zoomOutButton").query();
        Assert.assertNotNull(zoomOut);
        clickOn("#zoomOutButton");
        clickOn("#zoomOutButton");
        Button zoomIn = lookup("#zoomInButton").query();
        Assert.assertNotNull(zoomIn);
        clickOn("#zoomInButton");
        clickOn("#zoomInButton");
        clickOn("#zoomInButton");
        clickOn("#zoomOutButton");

        Button endPhase = lookup("#endPhaseButton").query();
        Assert.assertNotNull(endPhase);
        clickOn(endPhase);

        WaitForAsyncUtils.waitForFxEvents();

        Button confirm = lookup("#confirm").query();
        assertNotNull(confirm);
    }

    @Override
    public void stop() throws Exception {
        scene.getWindow().centerOnScreen();
    }


}
