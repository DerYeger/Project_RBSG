package de.uniks.se19.team_g.project_rbsg.egg;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class EggTest extends ApplicationTest {

    @Test
    public void testTheEgg() {
        final EasterEggController easterEggController =
                new EasterEggController(new SimpleObjectProperty<>(Locale.ENGLISH));
        Platform.runLater(easterEggController::start);
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(lookup("stage"));
    }
}
