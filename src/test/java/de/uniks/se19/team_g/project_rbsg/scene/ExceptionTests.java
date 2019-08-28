package de.uniks.se19.team_g.project_rbsg.scene;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder.Text.SOCKET;
import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.LOGIN;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        UserProvider.class,
        TitleViewBuilder.class,
        TitleViewController.class,
})
public class ExceptionTests  {

    @Test
    public void testExceptionHandling() {
        final SceneManager sceneManager = mock(SceneManager.class);
        doCallRealMethod()
                .when(sceneManager)
                .setScene(any());

        final AlertBuilder alertBuilder = mock(AlertBuilder.class);

        final Runnable onRetry = mock(Runnable.class);
        final Runnable onCancel = mock(Runnable.class);

        final ExceptionHandler exceptionHandler = new WebSocketExceptionHandler(alertBuilder)
                .onRetry(onRetry)
                .onCancel(onCancel);

        doThrow(new RuntimeException(new WebSocketException("test")))
                .when(sceneManager)
                .unhandledSetScene(any());

        sceneManager
                .setScene(SceneConfiguration
                        .of(LOGIN)
                        .withExceptionHandler(exceptionHandler));

        verify(alertBuilder)
                .priorityConfirmation(SOCKET, onRetry, onCancel);
    }
}
