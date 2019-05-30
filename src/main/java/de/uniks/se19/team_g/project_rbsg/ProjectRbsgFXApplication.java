package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Jan Müller
 */
@Component
public class ProjectRbsgFXApplication extends Application {

    public static final int WIDTH = 1336;
    public static final int HEIGHT = 768;

    private ConfigurableApplicationContext context;

    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Initializes the Spring context with passed arguments
     */
    @Override
    public void init() {
        ApplicationContextInitializer<AnnotationConfigApplicationContext> contextInitializer = applicationContext -> {
            applicationContext.registerBean(Parameters.class, this::getParameters);
            applicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        this.context = new SpringApplicationBuilder()
                .sources(ProjectRbsgApplication.class)
                .initializers(contextInitializer)
                .web(WebApplicationType.NONE)
                .run(getParameters().getRaw().toArray(new String[0]));
    }


    @Override
    public void start(@NotNull final Stage primaryStage) throws IOException {
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setResizable(false);

        context.getBean(SceneManager.class)
                .init(primaryStage)
                .setLoginScene();

        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stopping application");
        //temporary fix
        context.getBean(ChatController.class).terminate();
        context.getBean(LobbyViewController.class).terminate();
        this.context.close();
        Platform.exit();
    }
}
