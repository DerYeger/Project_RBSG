package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class GetUnitTypesService {

    final RestTemplate rbsgTemplate;
    private UnitTypeAdapter unitTypeAdapter;


    public GetUnitTypesService(@Nonnull RestTemplate rbsgTemplate, @Nullable UnitTypeAdapter unitTypeAdapter) {
        this.rbsgTemplate = rbsgTemplate;
        this.unitTypeAdapter = unitTypeAdapter != null ? unitTypeAdapter : new UnitTypeAdapter();
    }

    public CompletableFuture<List<Unit>> queryUnitPrototypes()
    {
        return CompletableFuture.supplyAsync(this::doQueryUnitTypes)
            .thenApply(
                    map -> map.stream().map(unitTypeAdapter::map).collect(Collectors.toList())
            )
        ;
    }

    private List<UnitType> doQueryUnitTypes() {
        final ResponseType response = rbsgTemplate.getForObject("/army/units", ResponseType.class);
        return Objects.requireNonNull(response).data;
    }

    public static class ResponseType extends RBSGDataResponse<List<UnitType>> {}
}
