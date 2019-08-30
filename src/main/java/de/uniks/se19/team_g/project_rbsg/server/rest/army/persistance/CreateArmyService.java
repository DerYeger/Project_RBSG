package de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.requests.PersistArmyRequest;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses.SaveArmyResponse;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;

@Component
public class CreateArmyService {

    final RestTemplate restTemplate;

    public CreateArmyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SaveArmyResponse createArmy(Army army) {

        PersistArmyRequest createArmyRequest = new PersistArmyRequest();
        ObservableList<Unit> units = army.units;
        createArmyRequest.units = new LinkedList<String>();

        for (Unit unit : units) {
            createArmyRequest.units.add(unit.id.get());
        }

        createArmyRequest.name = army.name.get();

        return restTemplate.postForObject(
                "/army",
                createArmyRequest,
                SaveArmyResponse.class
        );
    }
}
