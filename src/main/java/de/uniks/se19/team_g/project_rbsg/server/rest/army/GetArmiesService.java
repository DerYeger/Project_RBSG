package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class GetArmiesService {

    private final RestTemplate rbsgTemplate;

    public GetArmiesService(RestTemplate rbsgTemplate) {
        this.rbsgTemplate = rbsgTemplate;
    }

    public CompletionStage<List<Army>> queryArmies() {
        return CompletableFuture.supplyAsync(this::doQueryArmies);
    }

    protected List<Army> doQueryArmies() {
        final Response response = rbsgTemplate.getForObject("/army", Response.class);

        return Objects.requireNonNull(response).data.stream().map(this::mapArmyData).collect(Collectors.toList());
    }

    private Army mapArmyData(Response.Army serverArmy) {
        final Army localArmy = new Army();
        localArmy.id.set(serverArmy.id);
        localArmy.name.set(serverArmy.name);
        localArmy.units.setAll(serverArmy.units.stream().map(this::mapServerUnit).collect(Collectors.toList()));

        return localArmy;
    }

    private Unit mapServerUnit(String id) {
        final Unit unit = new Unit();
        unit.id.set(id);
        return null;
    }

    public static class Response extends RBSGDataResponse<List<Response.Army>> {
        public static class Army {
            public String id;
            public String name;
            public List<String> units;
        }
    }

}
