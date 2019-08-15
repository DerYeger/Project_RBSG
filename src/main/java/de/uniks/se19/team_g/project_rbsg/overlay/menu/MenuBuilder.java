package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayException;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.credits.CreditsBuilder;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class MenuBuilder implements ApplicationContextAware, Rincled {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private final OverlayTargetProvider overlayTargetProvider;
    private final Property<Locale> selectedLocale;
    @NonNull
    private final CreditsBuilder creditsBuilder;
    private final MusicManager musicManager;

    public MenuBuilder(@NonNull final OverlayTargetProvider overlayTargetProvider,
                       @NonNull final Property<Locale> selectedLocale,
                       @NonNull final CreditsBuilder creditsBuilder,
                       @NonNull final MusicManager musicManager) {
        this.overlayTargetProvider = overlayTargetProvider;
        this.selectedLocale = selectedLocale;
        this.creditsBuilder = creditsBuilder;
        this.musicManager = musicManager;
    }

    public void lobbyMenu(@NonNull final List<Tuple<String, Node>> entries) {
        try {
            entries.add(0, new Tuple<>("credits", creditsButton()));
            menu(entries).show();
        } catch (final OverlayException e) {
            logger.info("Unable to create menu: " + e.getMessage());
        }
    }

    public void battlefieldMenu(@NonNull final List<Tuple<String, Node>> entries) {
        try {
            menu(entries).show();
        } catch (final OverlayException e) {
            logger.info("Unable to create menu: " + e.getMessage());
        }
    }

    private Menu menu(@NonNull final List<Tuple<String, Node>> entries) throws OverlayException {
        final OverlayTarget target = overlayTargetProvider.getOverlayTarget();
        entries.add(0, new Tuple<>("music", musicManager.newButton()));
        if (target == null) {
            throw new OverlayException("No target available");
        }

        if (!target.canShowOverlay()) {
            throw new OverlayException("An overlay is already active");
        }

        @SuppressWarnings("unchecked")
        final ViewComponent<Menu> components = (ViewComponent<Menu>) context.getBean("menuView");
        final Menu menu = components.getController();
        menu
                .setLocale(selectedLocale)
                .setEntries(entries)
                .initialize(
                        "menu",
                        components.getRoot(),
                        target
                );

        return menu;
    }

    private Button creditsButton() {
        final Button creditsButton = new Button();
        creditsButton.getStyleClass().addAll("icon-button");
        JavaFXUtils.setButtonIcons(
                creditsButton,
                getClass().getResource("/assets/icons/navigation/heartWhite.png"),
                getClass().getResource("/assets/icons/navigation/heartBlack.png"),
                30
        );
        creditsButton.setOnAction(event -> creditsBuilder.credits());

        return creditsButton;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
