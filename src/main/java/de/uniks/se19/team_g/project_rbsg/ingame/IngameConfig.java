package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.BattleFieldController;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.state.GameEventDispatcher;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.WaitingRoomViewController;
import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import java.util.Objects;

@Configuration
public class IngameConfig {

    private final ObjectFactory<GameEventManager> eventManagerFactory;
    private final ObjectFactory<ModelManager> modelManagerFactory;
    private final ObjectFactory<GameEventDispatcher> dispatcherFactory;

    public IngameConfig(ObjectFactory<GameEventManager> eventManagerFactory, ObjectFactory<ModelManager> modelManagerFactory, ObjectFactory<GameEventDispatcher> dispatcherFactory) {
        this.eventManagerFactory = eventManagerFactory;
        this.modelManagerFactory = modelManagerFactory;
        this.dispatcherFactory = dispatcherFactory;
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<WaitingRoomViewController> waitingRoom(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/waiting_room/waitingRoomView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<BattleFieldController> battleFieldScene(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/ingame/battleFieldView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public IngameContext autoContext(
            @Nonnull UserProvider userProvider,
            @Nonnull GameProvider gameDataProvider,
            @Qualifier("dedicatedContext") ObjectProvider<IngameContext> contextFactory
    ) {
        return contextFactory.getObject(userProvider.get(), gameDataProvider.get());
    }

    @Bean
    @Scope("prototype")
    public IngameContext dedicatedContext(
            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") @Nonnull User user,
            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") @Nonnull Game gameData
    ) {
        Objects.requireNonNull(gameData);

        GameEventManager gameEventManager = eventManagerFactory.getObject();
        GameEventDispatcher dispatcher = dispatcherFactory.getObject();
        ModelManager modelManager = modelManagerFactory.getObject();

        IngameContext ingameContext = new IngameContext(user, gameData);

        ingameContext.setModelManager(modelManager);
        ingameContext.setGameEventManager(gameEventManager);
        dispatcher.setModelManager(modelManager);

        gameEventManager.addHandler(modelManager);
        gameEventManager.addHandler(dispatcher);
        gameEventManager.addHandler(new GameInitFinishedListener(ingameContext));

        return ingameContext;
    }
}
