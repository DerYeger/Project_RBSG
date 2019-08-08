package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.ServerConfig;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.DeleteArmyService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.SaveFileStrategy;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.requests.PersistArmyRequest;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses.SaveArmyResponse;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.ApiClientErrorInterceptor;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServerConfig.class,
        LoginManager.class,
        UserProvider.class,
        UserKeyInterceptor.class,
        ApiClientErrorInterceptor.class,
        ObjectMapper.class,
        ApplicationState.class
})

public class PersistantArmyTest {

    @Nullable
    @Autowired
    PersistentArmyManager persistantArmyManager;

    @Autowired
    LoginManager loginManager;
    @Autowired
    UserProvider userProvider;
    @Autowired
    RestTemplate rbsgTemplate;
    @MockBean
    DeleteArmyService deleteArmyService;
    @MockBean
    GetArmiesService getArmiesService;
    @Autowired
    ApplicationState applicationState;

    private String armyId;

    @Ignore
    @Test
    public void testCreateArmyRemote() throws ExecutionException, InterruptedException {
        Army army = new Army();
        User user = new User("test123", "test123");
        user.setUserKey(
                Objects.requireNonNull(loginManager.onLogin(user).get().getBody()).get("data").get("userKey").asText()
        );
        userProvider.set(user);

        for(int i=0; i<10; i++){
            Unit unit = new Unit();
            unit.id.set("5cc051bd62083600017db3b6");
            army.units.add(unit);
        }

        army.name.set("ggArmyFromTest");

        CompletableFuture<SaveArmyResponse> saveArmyResponse = persistantArmyManager.saveArmyOnline(army);

        sleep(2000);

        Assert.assertNotNull(army.id.get());
        Assert.assertEquals("success", saveArmyResponse.get().status);
        Assert.assertNotNull(saveArmyResponse.get().data.id);
        Assert.assertFalse(saveArmyResponse.get().data.units.isEmpty());
        Assert.assertEquals(10, saveArmyResponse.get().data.units.size());

        armyId=army.id.get();
    }

    @Test
    public void testCreateArmyLocal() throws ExecutionException, InterruptedException {
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
        final SaveFileStrategy fileStrategy = mock(SaveFileStrategy.class);

        persistantArmyManager = new PersistentArmyManager(
                restMock,
                deleteArmyService,
                getArmiesService,
                fileStrategy
        );

        //Assess persistArmyManger

        SaveArmyResponse response = persistantArmyManager.saveArmyOnline(army).get();
        verify(restMock).postForObject(eq("/army"), isA(PersistArmyRequest.class), eq(SaveArmyResponse.class));
        Assert.assertNotNull(army.id.get());
        Assert.assertEquals("200", response.status);
        Assert.assertNotNull(response.data.id);
        Assert.assertFalse(response.data.units.isEmpty());
        Assert.assertEquals(10, response.data.units.size());

        verifyZeroInteractions(fileStrategy);
    }

    //Deprecated
    @Ignore
    @Test
    public void testUpdateArmyRemote() throws ExecutionException, InterruptedException{
        //create new army on the server
        testCreateArmyRemote();

        Army myArmy=new Army();
        User user = new User("test123", "test123");
        user.setUserKey(
                Objects.requireNonNull(loginManager.onLogin(user).get().getBody()).get("data").get("userKey").asText()
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

        Assert.assertNotNull(myArmy.id.get());
        Assert.assertEquals("success", saveArmyResponse.get().status);
        Assert.assertTrue(saveArmyResponse.get().message.contains("5cc051bd62083600017db3b7"));
        Assert.assertFalse(saveArmyResponse.get().message.contains("5cc051bd62083600017db3b6")); //The updated army doesnt contain any of the old units
        Assert.assertTrue(saveArmyResponse.get().message.contains(myArmy.id.get()));
        Assert.assertTrue(saveArmyResponse.get().message.contains(myArmy.name.get()));

        /* Server Response on success : 18.06.2019
         *
         * "{"status":"success","message":"{\"id\":\"5d07fc8577af9d0001476d90\",\"name\":\"ggArmyFromTest\",\
         * "units\":[\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",
         * \"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",
         * \"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",
         * \"5cc051bd62083600017db3b7\"]}","data":{}}"
         *
         * */
    }

    @Test
    public void saveArmiesLocalTest() throws IOException{

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        RestTemplate localTemplateMock = mock(RestTemplate.class);

        UserProvider userProvider1 = new UserProvider();
        User user1 = userProvider1.get();
        user1.setName("Test1");

        UserProvider userProvider2 = new UserProvider();
        User user2 = userProvider2.get();
        user2.setName("Test2");

        ArrayList<UserProvider> providerList = new ArrayList<>();
        Collections.addAll(providerList, userProvider1, userProvider2);

        for(UserProvider userProvider : providerList) {
            ArrayList<Army> armyList=new ArrayList<>();
            armies=new ArrayList<>();

            SaveFileStrategy fileStrategy = new SaveFileStrategy(userProvider);
            PersistentArmyManager persistentArmyManager = new PersistentArmyManager(
                    localTemplateMock,
                    deleteArmyService,
                    getArmiesService,
                    fileStrategy
            );

            String armyString;

            Unit unit1 = new Unit();
            unit1.id.set("5cc051bd62083600017db3b7");
            Unit unit2 = new Unit();
            unit2.id.set("5cc051bd62083600017db3b7");

            Army army = new Army();
            army.name.set(userProvider.get().getName() + "_army");
            army.units.add(unit1);
            army.units.add(unit2);

            Army army2 = new Army();
            army2.name.set(userProvider.get().getName() + "_army2");
            Unit unit3 = new Unit();
            unit3.id.set("5cc051bd62083600017db3b7");
            army2.units.add(unit3);

            Army army3 = new Army();
            army3.name.set(userProvider.get().getName() + "_army3");

            armyList.add(army);
            armyList.add(army2);
            armyList.add(army3);

            persistentArmyManager.saveArmiesLocal(armyList);

            File file = new File(persistentArmyManager.getSaveFile().getAbsolutePath());
            Assert.assertTrue(file.exists());
            Assert.assertTrue(file.canRead());
            Assert.assertTrue(file.isFile());

            armyString = Files.readString(Paths.get(file.getPath()));
            PersistentArmyManager.ArmyWrapper armyWrapper = objectMapper.readValue(armyString, PersistentArmyManager.ArmyWrapper.class);

            for (PersistentArmyManager.DeserializableArmy deserializableArmy : armyWrapper.armies) {
                Army newArmy = new Army();
                newArmy.id.set(deserializableArmy.id);
                newArmy.name.set(deserializableArmy.name);
                for (String unitId : deserializableArmy.units) {
                    Unit unit = new Unit();
                    unit.id.set(unitId);
                    newArmy.units.add(unit);
                }
                armies.add(newArmy);
            }

            Assert.assertEquals(3, armies.size());
            Assert.assertEquals(userProvider.get().getName() + "_army", armies.get(0).name.get());
            Assert.assertEquals(2, armies.get(0).units.size());
            Assert.assertEquals(userProvider.get().getName() + "_army2", armies.get(1).name.get());
            Assert.assertEquals(1, armies.get(1).units.size());
            Assert.assertEquals(userProvider.get().getName() + "_army3", armies.get(2).name.get());
            Assert.assertEquals(0, armies.get(2).units.size());
        }
    }
}