package de.uniks.se19.team_g.project_rbsg.scene;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.login.StartViewController;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.LOGIN;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SceneManagerConfig.class,
        FXMLLoaderFactory.class,
        SceneManagerTests.ContextConfiguration.class,
})
public class SceneManagerTests extends ApplicationTest implements ApplicationContextAware {

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public StartViewController startViewController() {
            return mock(StartViewController.class);
        }
    }

    private ApplicationContext applicationContext;

    private SceneManager sceneManager;

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start(@NonNull final Stage stage) {
        sceneManager = new SceneManager() {
            @Override
            void initStage() {

            }
        };
        sceneManager.setApplicationContext(applicationContext);
        sceneManager.init(stage);
        stage.show();
    }


    @Test
    public void testSetScene() {
        sceneManager.setScene(SceneConfiguration.of(LOGIN));
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(lookup("#musicButton").query());
    }
}
