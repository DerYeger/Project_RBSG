package de.uniks.se19.team_g.project_rbsg.overlay.credits;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayException;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import javafx.beans.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Juri Lozowoj
 */
@Component
public class CreditsBuilder implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private final OverlayTargetProvider overlayTargetProvider;
    private final Property<Locale> selectedLocale;

    public CreditsBuilder(@NonNull final OverlayTargetProvider overlayTargetProvider,
                          @NonNull final Property<Locale> selectedLocale) {
        this.overlayTargetProvider = overlayTargetProvider;
        this.selectedLocale = selectedLocale;
    }

    public void credits() {
        try {
            build().show();
        } catch (final OverlayException e) {
            logger.info(e.getMessage());
        }
    }

    private Credits build() throws OverlayException {
        final OverlayTarget target = overlayTargetProvider.getOverlayTarget();

        if (target == null) {
            throw new OverlayException("No target available");
        }

        target.hideAllOverlays();

        @SuppressWarnings("unchecked")
        final ViewComponent<Credits> components = (ViewComponent<Credits>) context.getBean("creditsView");
        final Credits credits = components.getController();
        credits
                .setLocale(selectedLocale)
                .initialize(
                        "credits",
                        components.getRoot(),
                        target
                );

        return credits;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
