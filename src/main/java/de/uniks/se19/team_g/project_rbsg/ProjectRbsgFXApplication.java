package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Jan Müller
 */
@Component
public class ProjectRbsgFXApplication extends Application implements Rincled {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 840;

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

        //Initialisiert den Resource Loader für Rincl (I18N)
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());

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

        Rincl.setLocale(Locale.ENGLISH);

        final SceneManager sceneManager = context.getBean(SceneManager.class);

        sceneManager.init(primaryStage)
                .setScene(SceneManager.SceneIdentifier.LOGIN, false, null);

        context.getBean(MusicManager.class).initMusic();

        primaryStage.setOnCloseRequest(event -> {
            sceneManager.showAlert(AlertBuilder.Type.EXIT);
            event.consume();
        });

        primaryStage.show();
    }

    @Override
    public void stop() {
        context.getBean(Terminator.class)
                .terminate();
        this.context.close();
    }

    //TODO add stylesheet
    private void showCloseDialog(@NonNull final WindowEvent event, @NonNull final String alertTitle) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(getResources().getString("closeDialoge"));
        alert.showAndWait();

        if (alert.getResult().equals(ButtonType.OK)) {
            //let event propagate and close
        } else {
            event.consume();
        }
    }
}
