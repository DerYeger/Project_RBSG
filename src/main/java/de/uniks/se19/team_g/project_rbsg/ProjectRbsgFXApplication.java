package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

/**
 * @author Jan Müller
 */
@Component
public class ProjectRbsgFXApplication extends Application implements Rincled {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 840;

    private ConfigurableApplicationContext context;

    @Autowired
    private Property<Locale> selectedLocale;

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

        Objects.requireNonNull(selectedLocale).setValue(Locale.ENGLISH);

        context.getBean(SceneManager.class)
                .init(primaryStage)
                .setStartScene();

        context.getBean(MusicManager.class).initMusic();

        primaryStage.setOnCloseRequest(event -> showCloseDialog(event, primaryStage.getTitle()));

        primaryStage.show();
    }

    @Override
    public void stop() {
        context.getBean(Terminator.class)
                .terminate();
        this.context.close();
        Platform.exit();
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
