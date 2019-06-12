package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class GetUnitTypesService {

    final RestTemplate rbsgTemplate;


    public GetUnitTypesService(RestTemplate rbsgTemplate) {
        this.rbsgTemplate = rbsgTemplate;
    }

    public CompletableFuture<List<UnitType>> queryUnitTypes()
    {
        return CompletableFuture.supplyAsync(this::doQueryUnitTypes);
    }

    private List<UnitType> doQueryUnitTypes() {
        final ResponseType response = rbsgTemplate.getForObject("/army/units", ResponseType.class);
        return Objects.requireNonNull(response).data;
    }

    private static class ResponseType extends RBSGDataResponse<List<UnitType>> {}
}
