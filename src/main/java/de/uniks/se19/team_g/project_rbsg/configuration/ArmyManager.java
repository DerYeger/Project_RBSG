package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetArmiesService;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

@Component
public class ArmyManager {

    @Nonnull private final GetArmiesService getArmiesService;

    public ArmyManager(@Nonnull GetArmiesService getArmiesService) {
        this.getArmiesService = getArmiesService;
    }

    @Nonnull
    public List<Army> getArmies() {
        return loadArmies();
    }

    private List<Army> loadArmies() {

        CompletableFuture<List<Army>> remoteArmiesFuture = getArmiesService.queryArmies();
        while (!remoteArmiesFuture.isDone()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CompletableFuture<List<Army>> mergedArmiesObject = remoteArmiesFuture.thenApply(remoteArmies -> mergeArmies(remoteArmies));

        List<Army> mergedArmies = null;
        try {
            mergedArmies = mergedArmiesObject.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return mergedArmies;
    }

    private List<Army> mergeArmies(List<Army> remoteArmies) {
        List<Army> localArmies = null;
        try {
            localArmies = getArmiesService.loadLocalArmies();
        } catch (IOException e) {
            e.printStackTrace();
            return remoteArmies;
        }
        ArrayList<Army> mergedArmies = new ArrayList<>();
        if(remoteArmies.isEmpty()){
            return localArmies;
        }

        for (Army remoteArmy : remoteArmies) {
            mergedArmies.add(remoteArmy);
            for (Army localArmy : localArmies) {
                if (localArmy.id.get()=="" && !mergedArmies.contains(localArmy)) {
                    mergedArmies.add(localArmy);
                    break;
                }
                if (remoteArmy.id.equals(localArmy.id) && !mergedArmies.contains(localArmy)) {
                    //Accept remote units but use image from localArmy
                    //ToDo: Image-attribute not implemented yet
                    mergedArmies.add(remoteArmy);
                }
            }
        }
        return mergedArmies;
    }
}
