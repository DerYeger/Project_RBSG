package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.view.SplashImageBuilder;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

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
        ApplicationContextInitializer<GenericApplicationContext> contextInitializer = applicationContext -> {
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
    public void start(@NotNull final Stage primaryStage) {
        Pane pane = new Pane();
        pane.setBackground(new Background(SplashImageBuilder.getSplashImage()));

        Scene scene = new Scene(pane);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}
