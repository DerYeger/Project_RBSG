package de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses.SaveArmyResponse;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.requests.PersistArmyRequest;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

@Component
public class PersistantArmyManager {
    final RestTemplate restTemplate;
    private final String url = "/army";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PersistantArmyManager(@NonNull RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    public CompletableFuture<SaveArmyResponse> saveArmyOnline(@NonNull Army army) throws InterruptedException {
        if(army.id.isEmpty().get()){
            return createArmy(army);
        }
        return updateArmy(army);
    }

    private CompletableFuture<SaveArmyResponse> updateArmy(@NonNull Army army){
        String putUrl = "/army/"+army.id.get();
        PersistArmyRequest updateArmyRequest = new PersistArmyRequest();
        updateArmyRequest.name=army.name.get();
        updateArmyRequest.units=new LinkedList<String>();

        for(Unit unit : army.units){
            updateArmyRequest.units.add(unit.id.get());
        }

        return CompletableFuture.supplyAsync(() -> restTemplate.exchange(putUrl,
                HttpMethod.PUT,
                new HttpEntity<>(updateArmyRequest),
                SaveArmyResponse.class))
                    .thenApply(updateAnswer -> onUpdateArmyResponseReturned(updateAnswer, army))
                    .exceptionally(this :: handleException);
    }

    private SaveArmyResponse onUpdateArmyResponseReturned(
            ResponseEntity<SaveArmyResponse> updateArmyResponse,
            Army army) {

        SaveArmyResponse saveArmyResponse = new SaveArmyResponse();

        if(updateArmyResponse.getBody().message.isEmpty()){
            logger.debug("Server responded with empty message.");
        }
        if(updateArmyResponse.getBody().status.isEmpty()){
            logger.debug("Server responded with empty status.");
        }
        if(updateArmyResponse.getBody().data!=null){
            logger.debug("Server responded with data: " + updateArmyResponse.getBody().data.toString());
        }

        saveArmyResponse.message=updateArmyResponse.getBody().message;
        saveArmyResponse.status=updateArmyResponse.getBody().status;
        saveArmyResponse.data=updateArmyResponse.getBody().data;

        return saveArmyResponse;
    }

    private CompletableFuture<SaveArmyResponse> createArmy(@NonNull Army army){
        PersistArmyRequest createArmyRequest = new PersistArmyRequest();
        ObservableList<Unit> units = army.units;;
        createArmyRequest.units=new LinkedList<String>();

        for(Unit unit : units){
            createArmyRequest.units.add(unit.id.get());
        }
        createArmyRequest.name=army.name.get();

        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(
                url,
                createArmyRequest,
                SaveArmyResponse.class))
                    .thenApply(saveArmyResponse -> onSaveArmyResponseReturned(saveArmyResponse, army))
                    .exceptionally(this::handleException);
    }

    private SaveArmyResponse onSaveArmyResponseReturned(
            @NonNull SaveArmyResponse saveArmyResponse,
            Army army){

        String armyId = saveArmyResponse.data.id;
        if(armyId.equals(null)){
            logger.debug("Server answer doesnt contain a armyID.");
            return null;
        }
        if(!armyId.isEmpty()){
            army.id.set(armyId);
        }
        else{
            logger.debug("Server responded without ID: ");
        }
        if(!saveArmyResponse.message.equals(null)){
            logger.debug("Server responded with message: " + saveArmyResponse.message);
        }
        return saveArmyResponse;
    }

    private SaveArmyResponse handleException(@NonNull Throwable throwable) {
        logger.debug("Army creation failed with message: " + throwable.getMessage());
        return null;
    }

    public void saveArmiesLocal(ArrayList<Army> armyList) {
        JSONObject localArmiesObject = new JSONObject();
        JSONArray armies = new JSONArray();
        ObjectMapper objectmapper = new ObjectMapper();
        int id = 0;
        for(Army army : armyList){
            LinkedList<String> idList = new LinkedList<>();
            JSONObject armyObject = new JSONObject();
            armyObject.put("id", army.id.get());
            armyObject.put("name", army.name.get());

            for(Unit unit : army.units){
                idList.add(unit.id.get());
            }
            armyObject.put("units", idList);

            if(army.id.isEmpty().get()){
                army.id.set("");
            }

            localArmiesObject.put(army.id.get(), armyObject);
            armies.put(armyObject);
        }

        try {
            String osType = System.getProperty("os.name");
            System.out.println(osType);
            File file;
            File directory;
            if(osType.contains("Windows")){
                directory = new File("%userprofile%/rbsg/");
                directory.mkdirs();
                file = new File(directory,"armies.json");
            }
            else{
                directory = new File("~/.local/rbsg/");
                directory.mkdirs();
                file = new File(directory, "armies.json");
            }
            if(file.exists()){
                file.delete();
            }
            else{
                file.createNewFile();
            }
            objectmapper.writeValue(file, armies.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkOs() {
        File file = new File(".");
        if(file.getAbsolutePath().contains("/")){
            return true;
        }
        return false;
    }

    public void saveArmies(ObservableList<Army> armies) throws InterruptedException {

        ArrayList<Army> armyList = new ArrayList<>();

        for(Army army : armies){
            if(army.units.size()==10){
                //army is complete
                this.saveArmyOnline(army);
            }
            armyList.add(army);
        }
        if(!armyList.isEmpty()){
            this.saveArmiesLocal(armyList);
        }
    }
}
