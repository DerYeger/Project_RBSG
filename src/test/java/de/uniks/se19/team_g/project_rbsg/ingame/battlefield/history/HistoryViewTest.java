package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.ActionImpl;
import de.uniks.se19.team_g.project_rbsg.ingame.state.History;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
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
})
public class HistoryViewTest extends ApplicationTest {

    @Autowired
    HistoryViewProvider historyViewProvider;

    @MockBean
    ModelManager modelManager;

    @MockBean
    ActionRenderer actionRenderer;

    @Test
    public void test() throws ExecutionException, InterruptedException {

        Unit dickBird = new Unit("dickBird");
        Unit chubbyCharles = new Unit ("chubbyCharles");

        Action action1 = new ActionImpl("position", null, dickBird);
        Action action2 = new ActionImpl("position", null, chubbyCharles);
        Action action3 = new ActionImpl("position", null, dickBird);

        IngameContext context = new IngameContext(
                new UserProvider(),
                new GameProvider(),
                new IngameGameProvider()
        );

        History history = new History();
        history.push(action1);
        history.push(action2);
        Mockito.when(modelManager.getHistory()).thenReturn(history);
        Mockito.when(actionRenderer.render(Mockito.any())).thenReturn(new Label("entry"));
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

        Assert.assertEquals(2, lookup("#history .list-cell")
                .lookup(nonEmptyMatcher).queryAll().size());

        CompletableFuture.runAsync(
                () -> history.push(action3),
                Platform::runLater
        ).get();

        Assert.assertEquals( 3, lookup("#history .list-cell")
                .lookup(nonEmptyMatcher).queryAll().size());

        Mockito.verify(
                actionRenderer,
                Mockito.times(3)
        ).render(Mockito.any());
    }
}