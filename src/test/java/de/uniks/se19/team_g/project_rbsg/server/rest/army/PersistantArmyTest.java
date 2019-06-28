package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.server.ServerConfig;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses.SaveArmyResponse;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.requests.PersistArmyRequest;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.ApiClientErrorInterceptor;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServerConfig.class,
        LoginManager.class,
        UserProvider.class,
        PersistentArmyManager.class,
        UserKeyInterceptor.class,
        ApiClientErrorInterceptor.class,
        ObjectMapper.class
})

public class PersistantArmyTest {

    @Autowired
    PersistentArmyManager persistantArmyManager;
    @Autowired
    LoginManager loginManager;
    @Autowired
    UserProvider userProvider;
    @Autowired
    RestTemplate rbsgTemplate;

    private String armyId;

    @Ignore
    @Test
    public void testCreateArmyRemote() throws ExecutionException, InterruptedException {
        Army army = new Army();
        User user = new User("test123", "test123");
        user.setUserKey(
                loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );
        userProvider.set(user);

        for(int i=0; i<10; i++){
            Unit unit = new Unit();
            unit.id.set("5cc051bd62083600017db3b6");
            army.units.add(unit);
        }

        army.name.set("ggArmyFromTest");
        System.out.println(user.getUserKey());;

        CompletableFuture<SaveArmyResponse> saveArmyResponse = persistantArmyManager.saveArmyOnline(army);

        sleep(2000);

        Assert.assertTrue(army.id.get()!=null);
        Assert.assertTrue(saveArmyResponse.get().status.equals("success"));
        Assert.assertTrue(saveArmyResponse.get().data.id!=null);
        Assert.assertFalse(saveArmyResponse.get().data.units.isEmpty());
        Assert.assertTrue(saveArmyResponse.get().data.units.size()==10);

        armyId=army.id.get();
    }

    @Test
    public void testCreateArmyLocal(){
        RestTemplate restMock = mock(RestTemplate.class);
        PersistentArmyManager persistantArmyManager;
        SaveArmyResponse saveArmyResponse = new SaveArmyResponse();
        saveArmyResponse.data=new SaveArmyResponse.SaveArmyResponseData();
        saveArmyResponse.data.units=new LinkedList<>();
        Army army = new Army();
        army.name.set("ggArmy");
        PersistArmyRequest persistArmyRequest = new PersistArmyRequest();
        persistArmyRequest.name=army.name.get();

        for(int i=0; i<10; i++){
            Unit unit = new Unit();
            unit.id.set("5cc051bd62083600017db3b6");
            army.units.add(unit);
            saveArmyResponse.data.units.add("5cc051bd62083600017db3b6");
        }
        persistArmyRequest.units=saveArmyResponse.data.units;
        saveArmyResponse.data.id="5d0686cc77af9d0001472f49";
        saveArmyResponse.data.name="ggArmy";
        saveArmyResponse.status="200";
        saveArmyResponse.message="";

        when(restMock.postForObject(eq("/army"), isA(PersistArmyRequest.class), eq(SaveArmyResponse.class)))
                .thenReturn(saveArmyResponse);

        persistantArmyManager = new PersistentArmyManager(restMock);

        //Assess persistArmyManger
        try {

            CompletableFuture<SaveArmyResponse> response = persistantArmyManager.saveArmyOnline(army);
            //will fail otherwise
            sleep(2000);
            verify(restMock).postForObject(eq("/army"), isA(PersistArmyRequest.class), eq(SaveArmyResponse.class));
            Assert.assertTrue(army.id.get()!=null);
            Assert.assertTrue(response.get().status.equals("200"));
            Assert.assertTrue(response.get().data.id!=null);
            Assert.assertFalse(response.get().data.units.isEmpty());
            Assert.assertTrue(response.get().data.units.size()==10);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void testUpdateArmyRemote() throws ExecutionException, InterruptedException{
        //create new army on the server
        testCreateArmyRemote();

        Army myArmy=new Army();
        User user = new User("test123", "test123");
        user.setUserKey(
                loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );

        userProvider.set(user);

        myArmy.name.set("ggArmy");
        myArmy.id.set(armyId);

        for(int i=0; i<10; i++){
            Unit unit = new Unit();
            unit.id.set("5cc051bd62083600017db3b7");
            myArmy.units.add(unit);
        }

        CompletableFuture<SaveArmyResponse> saveArmyResponse = persistantArmyManager.saveArmyOnline(myArmy);

        sleep(2000);

        Assert.assertTrue(myArmy.id.get()!=null);
        Assert.assertTrue(saveArmyResponse.get().status.equals("success"));
        Assert.assertTrue(saveArmyResponse.get().message.contains("5cc051bd62083600017db3b7"));
        Assert.assertFalse(saveArmyResponse.get().message.contains("5cc051bd62083600017db3b6")); //The updated army doesnt contain any of the old units
        Assert.assertTrue(saveArmyResponse.get().message.contains(myArmy.id.get()));
        Assert.assertTrue(saveArmyResponse.get().message.contains(myArmy.name.get()));

        /** Server Response on success : 18.06.2019
         *
         * "{"status":"success","message":"{\"id\":\"5d07fc8577af9d0001476d90\",\"name\":\"ggArmyFromTest\",\
         * "units\":[\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",
         * \"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",
         * \"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",
         * \"5cc051bd62083600017db3b7\"]}","data":{}}"
         *
         * **/
    }

    @Test
    public void testUpdateArmyLocal() throws InterruptedException, ExecutionException, MalformedURLException, URISyntaxException {
        RestTemplate localUpdateMock = mock(RestTemplate.class);
        SaveArmyResponse onUpdateResponse = new SaveArmyResponse();
        PersistentArmyManager armyManager = new PersistentArmyManager(localUpdateMock);
        onUpdateResponse.status="success";
        onUpdateResponse.message="\"id\":\"5d07fc8577af9d0001476d90\",\"name\":\"ggArmyFromTest\",\"units\":[\"5cc051bd62083600017db3b7\"]}";
        onUpdateResponse.data=new SaveArmyResponse.SaveArmyResponseData();
        ResponseEntity onUpdateResponseEntity = new ResponseEntity(onUpdateResponse, HttpStatus.ACCEPTED);

        Army myArmy = new Army();
        String armyId="5d07fc8577af9d0001476d90";

        myArmy.name.set("ggArmy");
        myArmy.id.set(armyId);

        for(int i=0; i<10; i++){
            Unit unit = new Unit();
            unit.id.set("5cc051bd62083600017db3b7");
            myArmy.units.add(unit);
        }
        when(localUpdateMock.exchange(eq("/army/"+myArmy.id.get()), eq(HttpMethod.PUT), isA(HttpEntity.class), eq(SaveArmyResponse.class)))
                .thenReturn(onUpdateResponseEntity);

        CompletableFuture<SaveArmyResponse> saveArmyResponse = armyManager.saveArmyOnline(myArmy);

        sleep(2000);

        verify(localUpdateMock).exchange(eq("/army/"+myArmy.id.get()), eq(HttpMethod.PUT), isA(HttpEntity.class), eq(SaveArmyResponse.class));
        Assert.assertTrue(myArmy.id.get()!=null);
        Assert.assertTrue(saveArmyResponse.get().status.equals("success"));
        Assert.assertTrue(saveArmyResponse.get().message.contains("5cc051bd62083600017db3b7"));
        Assert.assertTrue(saveArmyResponse.get().message.contains(myArmy.id.get()));
        Assert.assertTrue(saveArmyResponse.get().message.contains(myArmy.name.get()));
    }

    @Test
    public void saveArmiesLocalTest() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ArrayList<Army> armyList=new ArrayList<>();
        ArrayList<Army> armies= new ArrayList<>();
        RestTemplate localTemplateMock = mock(RestTemplate.class);
        PersistentArmyManager persistentArmyManager = new PersistentArmyManager(localTemplateMock);
        String armyString;

        Unit unit1 = new Unit();
        unit1.id.set("5cc051bd62083600017db3b7");
        Unit unit2 = new Unit();
        unit2.id.set("5cc051bd62083600017db3b7");

        Army army = new Army();
        army.name.set("ggArmyFromLocalTest");
        army.units.add(unit1);
        army.units.add(unit2);

        Army army2 = new Army();
        army2.name.set("ggArmyFromLocalTest2");
        Unit unit3 = new Unit();
        unit3.id.set("5cc051bd62083600017db3b6");
        army2.units.add(unit3);

        Army army3 = new Army();
        army3.name.set("ggArmyFromLocalTest3");

        armyList.add(army);
        armyList.add(army2);
        armyList.add(army3);

        persistentArmyManager.saveArmiesLocal(armyList);

        File file = new File(System.getProperty("user.home") + "/.local/rbsg/armies.json");
        System.out.println(file.getAbsoluteFile());
        Assert.assertTrue(file.exists());
        Assert.assertTrue(file.canRead());
        Assert.assertTrue(file.isFile());

        armyString = Files.readString(Paths.get(file.getPath()));
        PersistentArmyManager.ArmyWrapper armyWrapper = objectMapper.readValue(armyString, PersistentArmyManager.ArmyWrapper.class);
        for(PersistentArmyManager.DeserializableArmy deserializableArmy : armyWrapper.armies){
            Army newArmy = new Army();
            newArmy.id.set(deserializableArmy.id);
            newArmy.name.set(deserializableArmy.name);
            for(Unit unit : deserializableArmy.units){
                newArmy.units.add(unit);
            }
            armies.add(newArmy);
        }

        Assert.assertTrue(armies.size()==3);
        Assert.assertTrue(armies.get(0).name.get().equals("ggArmyFromLocalTest"));
        Assert.assertTrue(armies.get(0).units.size()==2);
        Assert.assertTrue(armies.get(1).name.get().equals("ggArmyFromLocalTest2"));
        Assert.assertTrue(armies.get(1).units.size()==1);
        Assert.assertTrue(armies.get(2).name.get().equals("ggArmyFromLocalTest3"));
        Assert.assertTrue(armies.get(2).units.size()==0);
    }
}