package de.uniks.se19.team_g.project_rbsg.overlay.egg;

import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import eu.yeger.minesweeper.Minesweeper;
import javafx.scene.Node;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class EasterEggBuilder {

    private final OverlayTargetProvider overlayTargetProvider;
    private final AlertBuilder alertBuilder;

    public EasterEggBuilder(@NonNull final OverlayTargetProvider overlayTargetProvider, @NonNull final AlertBuilder alertBuilder) {
        this.overlayTargetProvider = overlayTargetProvider;
        this.alertBuilder = alertBuilder;
    }

    public void easterEgg() {
        final Node minesweeper = Minesweeper
                .builder()
                .bombCount(25)
                .width(16)
                .height(16)
                .onGameWon(this::onGameWon)
                .onGameLost(this::onGameLost)
                .cellSize(40)
                .build()
                .instance();
        new EasterEgg(overlayTargetProvider.getOverlayTarget(), minesweeper).show();
    }

    private void onGameWon() {
        alertBuilder.priorityInformation(AlertBuilder.Text.GAME_WON, null);
    }

    private void onGameLost() {
        alertBuilder.priorityInformation(AlertBuilder.Text.GAME_LOST, null);
    }
}
