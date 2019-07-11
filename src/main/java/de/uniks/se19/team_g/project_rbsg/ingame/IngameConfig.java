package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.WaitingRoomViewController;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

@Configuration
public class IngameConfig {
    @Bean
    @Scope("prototype")
    public ViewComponent<WaitingRoomViewController> waitingRoom(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/waiting_room/waitingRoomView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<IngameViewController> battleFieldScene(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/ingame/battleFieldView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
