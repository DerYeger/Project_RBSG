package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.OverlayConfiguration;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        OverlayConfiguration.class,
        FXMLLoaderFactory.class,
        Menu.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MenuTests extends ApplicationTest implements ApplicationContextAware, OverlayTargetProvider {

    private static final Property<Locale> locale = new SimpleObjectProperty<>(Locale.ENGLISH);

    private MenuBuilder menuBuilder = new MenuBuilder(this);
    private OverlayTarget overlayTarget;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        menuBuilder.setApplicationContext(applicationContext);
    }

    @Override
    public void start(@NonNull final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
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
    public void testMenuCreation() {
        final Tuple<String, Node> entry = new Tuple<>("music", new Button("Hello there"));
        menuBuilder.battlefieldMenu(locale, Collections.singletonList(entry));

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(lookup("Music"));
        assertNotNull(lookup("Hello there"));
    }
}
