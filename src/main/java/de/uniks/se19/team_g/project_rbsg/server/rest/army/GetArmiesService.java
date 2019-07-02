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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class GetArmiesService {

    private String fileName="armies.json";

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

    public List<Army> loadArmies(){
        List<Army> remoteArmies = null;
        List<Army> localArmies = null;
        try {
            remoteArmies = queryArmies().get();
            localArmies = loadLocalArmies();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mergeArmies(remoteArmies, localArmies);
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
            file = new File(System.getProperty("user.home") + "\\.rbsg\\"+fileName);
            if (file.exists()) {
                armyString = Files.readString(Paths.get(file.getPath()));
            } else {
                file.createNewFile();
            }
        } else {
            //Unix
            file = new File(System.getProperty("user.home") + "/.local/rbsg/"+fileName);
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
            //Ensure army-size in case of malformed file.
            if(armies.size()<7){
                armies.add(newArmy);
            }
        }
        return armies;
    }

    private String checkOs() {
        String os = System.getProperty("os.name");
        return os;
    }

    public void setTestFileName(String fileName){
        this.fileName=fileName;
    }

    private List<Army> mergeArmies(List<Army> remoteArmies, List<Army> localArmies) {

        ArrayList<Army> mergedArmies = new ArrayList<>();
        if(remoteArmies.isEmpty()){
            return localArmies;
        }

        mergedArmies.addAll(remoteArmies);
        for (Army remoteArmy : remoteArmies) {
            for (Army localArmy : localArmies) {
                if (localArmy.id.get()=="" && !mergedArmies.contains(localArmy) && mergedArmies.size()<7) {
                    mergedArmies.add(localArmy);
                    break;
                }
                if (remoteArmy.id.equals(localArmy.id) && !mergedArmies.contains(localArmy)) {
                    //Accept remote units but use image from localArmy
                    //ToDo: Image-attribute not implemented yet
                }
            }
        }
        return mergedArmies;
    }

    public static class Response extends RBSGDataResponse<List<Response.Army>> {
        public static class Army {
            public String id;
            public String name;
            public List<String> units;
        }
    }

}
