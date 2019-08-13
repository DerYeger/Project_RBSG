package de.uniks.se19.team_g.project_rbsg.overlay;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import io.rincl.Rincled;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

public class MenuBuilder implements ApplicationContextAware, Rincled {

    private ApplicationContext context;

    private OverlayTargetProvider overlayTargetProvider;

    public MenuBuilder(@NonNull final OverlayTargetProvider overlayTargetProvider) {
        this.overlayTargetProvider = overlayTargetProvider;
    }

    public Menu battlefieldMenu() throws OverlayException {
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
        menu.initialize(
                getResources().getString("menu"),
                components.getRoot(),
                target);
        return menu;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
