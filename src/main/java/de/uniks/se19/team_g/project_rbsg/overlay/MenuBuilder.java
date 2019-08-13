package de.uniks.se19.team_g.project_rbsg.overlay;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import io.rincl.Rincled;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuBuilder implements ApplicationContextAware, Rincled {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private OverlayTargetProvider overlayTargetProvider;

    public MenuBuilder(@NonNull final OverlayTargetProvider overlayTargetProvider) {
        this.overlayTargetProvider = overlayTargetProvider;
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
                .withEntries(entries)
                .initialize(
                        getResources().getString("menu"),
                        components.getRoot(),
                        target
                );
        return menu;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
