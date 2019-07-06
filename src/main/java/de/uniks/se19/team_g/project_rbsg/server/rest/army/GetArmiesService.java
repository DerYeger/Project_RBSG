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
        int playableArmyCounter=0;

        ArrayList<Army> mergedArmies = new ArrayList<>();

        if(remoteArmies.isEmpty()){

            logger.debug("Remote armies are empty. Returning local armies.");

            for(Army army : localArmies){
                playableArmyCounter+=(army.units.size()==10) ? 1:0;
            }

            if(playableArmyCounter==0 && localArmies.size()==7){
                //ToDiscuss: Ensure that the application state will generate at least one playable army.
                logger.debug("Every local army isnt playbale. " +
                        "The first will be deleted, to ensure the creation on one playable army.");
                localArmies.remove(0);
            }

            return localArmies;
        }

        //Add all remoteArmies to
        mergedArmies.addAll(remoteArmies);
        playableArmyCounter+=remoteArmies.size();

        logger.debug(playableArmyCounter + " playable remote armies have been loaded.");

        for (Army remoteArmy : remoteArmies) {
            for (Army localArmy : localArmies) {

                if (localId != null && !localId.isBlank() && !mergedArmies.contains(localArmy) && mergedArmies.size()<7) {

                    logger.debug("Added local army with " + localArmy.units.size() + "units");
                    mergedArmies.add(localArmy);
                    if(localArmy.units.size()==10){

                        playableArmyCounter++;

                    }
                }
                if (remoteArmy.id.equals(localArmy.id) && !mergedArmies.contains(localArmy)) {

                    //Accept remote units but use image from localArmy
                    logger.debug("Local and remote army represent identical object. Local picture has been used.");
                    remoteArmy.iconType.set(localArmy.iconType.get());

                }
            }
        }
        if(playableArmyCounter==0){

            logger.debug("No playable army could be loaded.");
            return null;

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
