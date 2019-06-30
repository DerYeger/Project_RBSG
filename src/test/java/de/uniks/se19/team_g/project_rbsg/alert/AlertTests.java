package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.AlertConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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

import java.util.Locale;

import static org.junit.Assert.*;

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        AlertTests.ContextConfiguration.class,
        SceneManagerConfig.class,
        AlertConfig.class,
        SceneManager.class,
        AlertBuilder.class,
        InfoAlertController.class,
        ConfirmationAlertController.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AlertTests extends ApplicationTest implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

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

    private SceneManager sceneManager;

    private Scene scene;

    @Override
    public void start(@NonNull final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        Rincl.setLocale(Locale.ENGLISH);
        sceneManager = context.getBean(SceneManager.class);
        sceneManager.init(stage);
        scene = new Scene(new StackPane(new Pane()), 350, 150);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testInfoAlert() {
        final AlertBuilder alertBuilder = context.getBean(AlertBuilder.class);
        alertBuilder.information(AlertBuilder.Text.INVALID_INPUT);
        WaitForAsyncUtils.waitForFxEvents();

        final Button confirm = lookup("#confirm").queryButton();
        assertNotNull(confirm);
        assertNotNull(lookup("Invalid input"));

        clickOn(confirm);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(1, sceneManager.getAlertTarget().getChildren().size());
    }

    @Test
    public void testConfirmationAlert() {
        final boolean[] confirmedAndCanceled = new boolean[2];
        final AlertBuilder alertBuilder = context.getBean(AlertBuilder.class);
        alertBuilder.confirmation(AlertBuilder.Text.INVALID_INPUT,
                        () -> confirmedAndCanceled[0] = true,
                        () -> confirmedAndCanceled[1] = true);
        WaitForAsyncUtils.waitForFxEvents();

        final Button confirm = lookup("#confirm").queryButton();
        assertNotNull(confirm);
        assertNotNull(lookup("Invalid input"));

        clickOn(confirm);
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(confirmedAndCanceled[0]);

        final Button cancel = lookup("#cancel").queryButton();
        assertNotNull(cancel);

        WaitForAsyncUtils.waitForFxEvents();
        clickOn(cancel);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(1, sceneManager.getAlertTarget().getChildren().size());
        assertTrue(confirmedAndCanceled[0]);
        assertTrue(confirmedAndCanceled[1]);
    }

    @Test
    public void testException() {
        scene.setRoot(new Pane());
        final AlertBuilder alertBuilder = context.getBean(AlertBuilder.class);
        alertBuilder.information(AlertBuilder.Text.INVALID_INPUT);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(scene.getRoot().getChildrenUnmodifiable().isEmpty());
    }

    @Test
    public void testAlreadyActiveException() {
        final int[] wrapper = new int[1];
        final AlertBuilder alertBuilder = context.getBean(AlertBuilder.class);
        alertBuilder.confirmation(AlertBuilder.Text.INVALID_INPUT,
                () -> wrapper[0] = 1,
                null);
        WaitForAsyncUtils.waitForFxEvents();

        final Button confirm = lookup("#confirm").queryButton();
        assertNotNull(confirm);
        assertNotNull(lookup("Invalid input"));

        alertBuilder.confirmation(AlertBuilder.Text.EXIT,
                () -> wrapper[0] = 2,
                null);
        WaitForAsyncUtils.waitForFxEvents();

        clickOn(lookup("#confirm").queryButton());

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(1, wrapper[0]);
    }
}
