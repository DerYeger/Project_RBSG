package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.ArmyIcon;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.SaveFileStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class GetArmiesService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Nonnull
    private final RestTemplate rbsgTemplate;
    @Nonnull
    private final ArmyAdapter armyAdapter;
    @NonNull
    private final ArmyUnitAdapter armyUnitAdapter;
    @Nonnull
    private final SaveFileStrategy saveFileStrategy;

    public GetArmiesService(
            @Nonnull RestTemplate rbsgTemplate,
            @Nonnull ArmyAdapter armyAdapter,
            @Nonnull ArmyUnitAdapter armyUnitAdapter,
            @Nonnull SaveFileStrategy saveFileStrategy
    ) {
        this.rbsgTemplate = rbsgTemplate;
        this.armyAdapter = armyAdapter;
        this.armyUnitAdapter = armyUnitAdapter;
        this.saveFileStrategy = saveFileStrategy;
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

        File file = saveFileStrategy.getSaveFile();
        if (!file.exists()) {
            return armies;
        }
        String armyString = Files.readString(file.toPath());

        if (armyString.equals("")) {
            return armies;
        }
        PersistentArmyManager.ArmyWrapper armyWrapper = objectMapper.readValue(armyString, PersistentArmyManager.ArmyWrapper.class);

        for (PersistentArmyManager.DeserializableArmy deserializableArmy : armyWrapper.armies) {

            Army newArmy = new Army();
            newArmy.id.set(deserializableArmy.id);
            newArmy.name.set(deserializableArmy.name);
            newArmy.iconType.set(ArmyIcon.resolveValue(deserializableArmy.armyIcon));

            for (String unitId : deserializableArmy.units) {
                newArmy.units.add(armyUnitAdapter.mapServerUnit(unitId));
            }

            //Ensure army-size in case of malformed file.
            if(armies.size()<7){
                armies.add(newArmy);
            }
            else{
                logger.debug(armies.size() + " armies have been detected. But only 7 can be progressed.");
            }
        }
        return armies;
    }

    private List<Army> mergeArmies(List<Army> remoteArmies, List<Army> localArmies) {

        ArrayList<Army> mergedArmies = new ArrayList<>();

        if(remoteArmies.isEmpty()){

            logger.debug("Remote armies are empty. Returning local armies.");

            return localArmies;
        }

        //Add all remoteArmies to
        mergedArmies.addAll(remoteArmies);

        for (Army remoteArmy : remoteArmies) {
            for (Army localArmy : localArmies) {
                final String remoteId = remoteArmy.id.get();
                final String localId = localArmy.id.get();

                if (localId != null && !localId.isBlank() && !mergedArmies.contains(localArmy) && mergedArmies.size()<7) {

                    logger.debug("Added local army with " + localArmy.units.size() + "units");
                    mergedArmies.add(localArmy);
                }
                if (remoteId.equals(localId) && !mergedArmies.contains(localArmy)) {

                    //Accept remote units but use image from localArmy
                    logger.debug("Local and remote army represent identical object. Local picture has been used.");
                    remoteArmy.iconType.set(localArmy.iconType.get());

                }
            }
        }

        return performanceBoostedArmySort(mergedArmies); // ;-)
    }

    private ArrayList<Army> performanceBoostedArmySort(ArrayList<Army> armies){
        ArrayList<Army> sortedArmies = new ArrayList<>();
        int smallest=0;
        int smallestHash = armies.get(smallest).id.get().hashCode();
        while(armies.size()>0) {
            for (int i = 0; i < armies.size(); i++) {
                int armyHash = armies.get(i).id.get().hashCode();
                if (smallestHash > armyHash) {
                    smallest = i;
                    smallestHash = armyHash;
                }
            }
            sortedArmies.add(armies.get(smallest));
            armies.remove(armies.get(smallest));
            smallest=0;
        }
        return sortedArmies;
    }

    public static class Response extends RBSGDataResponse<List<Response.Army>> {
        public static class Army {
            public String id;
            public String name;
            public List<String> units;
        }
    }

}
