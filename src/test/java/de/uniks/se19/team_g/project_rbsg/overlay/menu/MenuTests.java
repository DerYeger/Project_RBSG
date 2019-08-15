package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.OverlayConfiguration;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.credits.CreditsBuilder;
import io.rincl.Rincl;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        LocaleConfig.class,
        Menu.class,
        OverlayConfiguration.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MenuTests extends ApplicationTest implements ApplicationContextAware, OverlayTargetProvider {

    private static final Property<Locale> locale = new SimpleObjectProperty<>(Locale.ENGLISH);

    private MenuBuilder menuBuilder;
    private OverlayTarget overlayTarget;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        @SuppressWarnings("unchecked")
        final Property<Locale> selectedLocale = (Property<Locale>) applicationContext.getBean("selectedLocale");
        menuBuilder = new MenuBuilder(this, selectedLocale, new CreditsBuilder(this, selectedLocale), new MusicManager());
        menuBuilder.setApplicationContext(applicationContext);
    }

    @Override
    public void start(@NonNull final Stage stage) {
        Rincl.setLocale(Locale.ENGLISH);
        overlayTarget = OverlayTarget.of(new Pane());
        final Scene scene = new Scene(overlayTarget, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public OverlayTarget getOverlayTarget() {
        return overlayTarget;
    }

    @Test
    public void testLobbyMenu() {
        menuBuilder.lobbyMenu();

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(lookup("Music"));
        assertNotNull(lookup("Language"));
        assertNotNull(lookup("Credits"));
    }

    @Test
    public void testBattlefieldMenu() {
        menuBuilder.battlefieldMenu(new ArrayList<>());

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(lookup("Music"));
        assertNotNull(lookup("Hello there"));
    }
}
