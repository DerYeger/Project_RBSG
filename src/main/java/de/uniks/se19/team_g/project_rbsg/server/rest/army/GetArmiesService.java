package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class GetArmiesService {

    @Nonnull private final RestTemplate rbsgTemplate;
    @Nonnull private final ArmyAdapter armyAdapter;

    public GetArmiesService(
        @Nonnull RestTemplate rbsgTemplate,
        @Nonnull ArmyAdapter armyAdapter
    ) {
        this.rbsgTemplate = rbsgTemplate;
        this.armyAdapter = armyAdapter;
    }

    public CompletableFuture<List<Army>> queryArmies() {
        return CompletableFuture.supplyAsync(this::doQueryArmies);
    }

    protected List<Army> doQueryArmies() {
        final Response response = rbsgTemplate.getForObject("/army", Response.class);

        return Objects.requireNonNull(response).data.stream().map(armyAdapter::mapArmyData).collect(Collectors.toList());
    }

    public static class Response extends RBSGDataResponse<List<Response.Army>> {
        public static class Army {
            public String id;
            public String name;
            public List<String> units;
        }
    }

}
