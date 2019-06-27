package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Biome;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

/**
 * @author  Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        IngameViewTests.ContextConfiguration.class,
        IngameViewController.class,
        IngameSceneBuilder.class,
        IngameViewBuilder.class
})
public class IngameViewTests extends ApplicationTest { // TODO Online Test ? for better coverage

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public IngameGameProvider ingameGameProvider() {
            return new IngameGameProvider(){
                @Override
                public Game get(){
                    Game game = new Game("game");
                    Cell cell1 = new Cell("1").setBiome(Biome.FOREST).setX(0).setY(0);
                    Cell cell2 = new Cell("2").setBiome(Biome.WATER).setX(0).setY(1);
                    Cell cell3 = new Cell("3").setBiome(Biome.MOUNTAIN).setX(1).setY(0);
                    Cell cell4 = new Cell("4").setBiome(Biome.GRASS).setX(1).setY(1);
                    cell1.setBottom(cell2);
                    cell1.setRight(cell3);
                    cell2.setTop(cell1);
                    cell2.setRight(cell1);
                    cell3.setBottom(cell4);
                    cell3.setLeft(cell1);
                    cell4.setTop(cell3);
                    cell4.setLeft(cell2);
                    cell1.setGame(game);
                    cell2.setGame(game);
                    cell3.setGame(game);
                    cell4.setGame(game);
                    game.withCells(cell1, cell2, cell3, cell4);
                    return game;
                }
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

    }

    @Autowired
    private IngameSceneBuilder ingameSceneBuilder;

    private Scene scene;

    @Override
    public void start(@NonNull final Stage stage) throws Exception {
        scene = ingameSceneBuilder.getIngameScene();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testBuildIngameView() throws Exception {
        Node ingameView = scene.getRoot();
        Assert.assertNotNull(ingameView);
        Canvas canvas = lookup("#canvas").query();
        Assert.assertNotNull(canvas);
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
    }

}
