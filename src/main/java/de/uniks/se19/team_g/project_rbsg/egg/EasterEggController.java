package de.uniks.se19.team_g.project_rbsg.egg;

import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import eu.yeger.minesweeper.Minesweeper;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.scene.Scene;
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

    private final Minesweeper config;
    private final Pane container;
    private final OverlayTarget overlayTarget;
    private final Stage stage;
    private final AlertBuilder alertBuilder;

    public EasterEggController(@NonNull final Property<Locale> selectedLocale) {
        config = Minesweeper
                .builder()
                .mineCount(25)
                .width(16)
                .height(16)
                .onGameWon(() -> onGameOver(AlertBuilder.Text.GAME_WON))
                .onGameLost(() -> onGameOver(AlertBuilder.Text.GAME_LOST))
                .cellSize(40)
                .build();

        container = new Pane();

        overlayTarget = OverlayTarget.of(container);

        stage = new Stage();
        stage.setScene(new Scene(overlayTarget));
        stage.setResizable(false);
        stage.titleProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "egg"));

        alertBuilder = new AlertBuilder(this);
    }

    public void start() {
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
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        alertBuilder.setApplicationContext(context);
    }
}
