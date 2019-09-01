package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UnitDeathAction;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UpdateAction;
import de.uniks.se19.team_g.project_rbsg.ingame.state.History;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        HistoryViewProvider.class,
        HistoryViewController.class,
        HistoryViewConfig.class,
})
public class HistoryViewTest extends ApplicationTest {

    @Autowired
    HistoryViewProvider historyViewProvider;

    @Autowired
    @Qualifier("actionRenderer")
    ActionRenderer renderer;

    @MockBean
    ModelManager modelManager;

    @MockBean(name = "mockRenderer")
    ActionRenderer mockRenderer;

    @Test
    public void test() throws ExecutionException, InterruptedException {

        Unit dickBird = new Unit("dickBird");
        dickBird.setUnitType(UnitTypeInfo._CHOPPER);
        Unit chubbyCharles = new Unit ("chubbyCharles");
        chubbyCharles.setUnitType(UnitTypeInfo._HEAVY_TANK);
        Player bob = new Player("bob");
        bob.setColor("blue");

        Action action0 = Mockito.mock(Action.class);
        Action action1 = new UpdateAction("position", new Cell("c1"), dickBird);
        Action action2 = new UpdateAction("hp", 5, chubbyCharles);
        Action action3 = new UpdateAction("currentPlayer", bob, new Game());
        Action action4 = new UnitDeathAction(chubbyCharles, bob);
        Action actionHidden = Mockito.mock(Action.class);

        IngameContext context = new IngameContext(null, null);

        History history = new History();
        history.push(action0);
        history.push(actionHidden);
        history.push(action1);
        history.push(action2);
        history.push(actionHidden);
        history.push(action4);

        int cellCountBase = 4;

        Mockito.when(modelManager.getHistory()).thenReturn(history);
        Mockito.when(mockRenderer.supports(Mockito.any(UpdateAction.class))).thenReturn(false);
        Mockito.when(mockRenderer.supports(action0)).thenReturn(true);
        Mockito.when(mockRenderer.render(action0)).thenReturn(new HistoryRenderData(new Label("entry"), null));
        context.setModelManager(modelManager);

        VBox root = new VBox();

        HistoryViewController controller = CompletableFuture.supplyAsync(
                () -> {
                    HistoryViewController controller1 = historyViewProvider.mountHistory(root, context);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                    return controller1;
                },
                Platform::runLater
        ).get();
        WaitForAsyncUtils.waitForFxEvents();

        Predicate<Node> nonEmptyMatcher = node -> !((ListCell) node).isEmpty();

        Assert.assertEquals(cellCountBase, lookup(".list-cell")
                .lookup(nonEmptyMatcher).queryAll().size());

        CompletableFuture.runAsync(
                () -> {
                    history.push(action3);
                    history.push(action0);
                    history.push(actionHidden);
                },
                Platform::runLater
        ).get();

        Assert.assertEquals( cellCountBase + 2, lookup(".list-cell")
                .lookup(nonEmptyMatcher).queryAll().size());

        Mockito.verify(
                mockRenderer,
                Mockito.atLeast(5)
        ).supports(Mockito.any());
        // actually not 100% sure why it's 3 and not 2...
        Mockito.verify(
                mockRenderer,
                Mockito.times(3)
        ).render(Mockito.any());
    }
}