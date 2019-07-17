package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;


import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
})



public class BattleFieldControllerTest extends ApplicationTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    @Override
    public void start(Stage stage){
        final Pane root = new Pane();

        stage.setScene(new Scene(root, 70, 70));
        stage.show();
    }

    @Test
    public void testSetHighlightingOnSelctedUnit(){
        Game game = new Game("1");
        Unit unit = new Unit("1");

        Cell cell = new Cell("1");
        cell.setBiome(Biome.GRASS);
        unit.setPosition(cell);

        unit.setGame(game);
        unit.setHp(10);
        unit.setMp(10);
        unit.setUnitType(UnitType.CHOPPER);

        IngameGameProvider ingameGameProvider = new IngameGameProvider();
        ingameGameProvider.set(game);

        IngameContext ingameContext = new IngameContext(new UserProvider(), new GameProvider(), ingameGameProvider);



    }

}
