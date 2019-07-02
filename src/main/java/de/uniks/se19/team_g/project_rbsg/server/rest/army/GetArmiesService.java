package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class GetArmiesService {

    private final String SAVE_ARMY_PATH = "src/main/resources/persistant/armies.json";

    @Nonnull
    private final RestTemplate rbsgTemplate;
    @Nonnull
    private final ArmyAdapter armyAdapter;
    @NonNull
    private final ArmyUnitAdapter armyUnitAdapter;

    public GetArmiesService(
            @Nonnull RestTemplate rbsgTemplate,
            @Nonnull ArmyAdapter armyAdapter,
            @NonNull ArmyUnitAdapter armyUnitAdapter
    ) {
        this.rbsgTemplate = rbsgTemplate;
        this.armyAdapter = armyAdapter;
        this.armyUnitAdapter = armyUnitAdapter;
    }

    public CompletableFuture<List<Army>> queryArmies() {
        return CompletableFuture.supplyAsync(this::doQueryArmies);
    }

    protected List<Army> doQueryArmies() {
        final Response response = rbsgTemplate.getForObject("/army", Response.class);

        return Objects.requireNonNull(response).data.stream().map(armyAdapter::mapArmyData).collect(Collectors.toList());
    }

    public List<Army> loadLocalArmies() throws IOException {
        List<Army> armies = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File file = null;
        String armyString = null;
        String operatingSystem = checkOs();

        if (operatingSystem.equals("Windows")) {
            //Windows
            file = new File(System.getProperty("user.home") + "\\.rbsg\\armies.json");
            if (file.exists()) {
                armyString = Files.readString(Paths.get(file.getPath()));
            } else {
                file.createNewFile();
            }
        } else {
            //Unix
            file = new File(System.getProperty("user.home") + "/.local/rbsg/armies.json");
            if (file.exists()) {
                armyString = Files.readString(Paths.get(file.getPath()));
            } else {
                file.createNewFile();
            }
        }

        if (armyString.equals("")) {
            return armies;
        }
        PersistentArmyManager.ArmyWrapper armyWrapper = objectMapper.readValue(armyString, PersistentArmyManager.ArmyWrapper.class);

        for (PersistentArmyManager.DeserializableArmy deserializableArmy : armyWrapper.armies) {
            Army newArmy = new Army();
            newArmy.id.set(deserializableArmy.id);
            newArmy.name.set(deserializableArmy.name);
            for (String unitId : deserializableArmy.units) {
                newArmy.units.add(armyUnitAdapter.mapServerUnit(unitId));
            }
            armies.add(newArmy);
        }
        return armies;
    }

    private String checkOs() {
        String os = System.getProperty("os.name");
        return os;
    }

    public static class Response extends RBSGDataResponse<List<Response.Army>> {
        public static class Army {
            public String id;
            public String name;
            public List<String> units;
        }
    }

}
