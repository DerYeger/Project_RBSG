package de.uniks.se19.team_g.project_rbsg.FeatureLobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.GameCreator;
import de.uniks.se19.team_g.project_rbsg.controller.CreateGameController;
import de.uniks.se19.team_g.project_rbsg.view.CreateGameFormBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.tomcat.jni.Time;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Juri Lozowoj
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, CreateGameFormBuilder.class, CreateGameController.class, GameCreator.class})
public class CreateGameViewTest extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Override

    public void start(@NonNull Stage stage) throws IOException{
        final Node createGameForm = context.getBean(CreateGameFormBuilder.class).getCreateGameForm();
        final Scene scene = new Scene((Parent) createGameForm);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test(){
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final String testGameName = "Noodles";
        sleep(300);
        clickOn(gameNameInput);
        eraseText(20);
        clickOn(gameNameInput);
        eraseText(20);
        write(testGameName);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals(testGameName, gameNameInput.getText());

        final ToggleButton twoPlayerButton = lookup("#twoPlayers").query();
        final ToggleButton fourPlayerButton = lookup("#fourPlayers").query();
        clickOn(twoPlayerButton);
        sleep(500);
        Assert.assertTrue(twoPlayerButton.isSelected());
        clickOn(fourPlayerButton);
        sleep(500);
        Assert.assertTrue(fourPlayerButton.isSelected());
    }
}
