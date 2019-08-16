package de.uniks.se19.team_g.project_rbsg.egg;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import eu.yeger.minesweeper.Minesweeper;
import io.rincl.Rincled;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope("prototype")
public class EasterEggController implements ApplicationContextAware, OverlayTargetProvider, Rincled {

    private final Property<Locale> selectedLocale;

    private final Minesweeper config;
    private final Pane container;
    private final OverlayTarget overlayTarget;
    private Stage stage;
    private final AlertBuilder alertBuilder;

    public EasterEggController(@NonNull final Property<Locale> selectedLocale) {
        this.selectedLocale = selectedLocale;
        config = Minesweeper
                .builder()
                .mineCount(25)
                .width(16)
                .height(16)
                .onGameWon(() -> onGameOver(AlertBuilder.Text.EGG_WON))
                .onGameLost(() -> onGameOver(AlertBuilder.Text.EGG_LOST))
                .cellSize(40)
                .build();

        container = new Pane();

        overlayTarget = OverlayTarget.of(container);

        alertBuilder = new AlertBuilder(this);
    }

    private void initStage() {
        stage = new Stage();
        stage.setScene(new Scene(overlayTarget));
        stage.setResizable(false);
        stage.titleProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "egg"));
        stage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("/assets/icons/icon.png")));
    }

    public void start() {
        if (stage == null) {
            initStage();
        }
        startNewGame();
        stage.show();
    }

    private void startNewGame() {
        container.getChildren().clear();
        container.getChildren().add(config.instance());
        stage.sizeToScene();
    }

    private void onGameOver(@NonNull final AlertBuilder.Text text) {
        alertBuilder.confirmation(
                text,
                this::startNewGame,
                stage::close
        );
    }

    @Override
    public OverlayTarget getOverlayTarget() {
        return overlayTarget;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        alertBuilder.setApplicationContext(context);
    }
}
