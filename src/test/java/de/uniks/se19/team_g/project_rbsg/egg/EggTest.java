package de.uniks.se19.team_g.project_rbsg.egg;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

import javafx.scene.layout.Pane;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;

import static org.junit.Assert.*;

public class EggTest extends ApplicationTest {

    @Test
    public void testTheEgg() {
        final EasterEggController easterEggController =
                new EasterEggController(new SimpleObjectProperty<>(Locale.ENGLISH));
        Platform.runLater(easterEggController::start);
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(lookup("stage"));
        assertEquals(
                20 * 40,
                lookup(node -> node instanceof Pane).queryAs(Pane.class).getWidth(),
                0
        );
        assertEquals(
                20 * 40,
                lookup(node -> node instanceof Pane).queryAs(Pane.class).getHeight(),
                0
        );
    }
}
