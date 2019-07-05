package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.ArmyManager;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.ServerConfig;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.DeleteArmyService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.ApiClientErrorInterceptor;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServerConfig.class,
        LoginManager.class,
        UserProvider.class,
        PersistentArmyManager.class,
        UserKeyInterceptor.class,
        ApiClientErrorInterceptor.class,
        ObjectMapper.class,
        GetArmiesService.class,
        ArmyAdapter.class,
        ArmyUnitAdapter.class,
        ApplicationState.class,
        DeleteArmyService.class,
        ArmyManager.class
})

public class LoadArmiesTest {


    @Autowired
    PersistentArmyManager persistantArmyManager;
    @Autowired
    LoginManager loginManager;
    @Autowired
    UserProvider userProvider;
    @Autowired
    RestTemplate rbsgTemplate;
    @Autowired
    GetArmiesService getArmiesService;
    @Autowired
    ArmyAdapter armyAdapter;
    @Autowired
    ArmyUnitAdapter armyUnitAdapter;
    @Autowired
    ApplicationState applicationState;
    @Autowired
    DeleteArmyService deleteArmyService;
    @Autowired
    ArmyManager armyManager;


    @Test
    public void testEmptyLocalLoading(){
        File localArmiesFile = new File(System.getProperty("user.home") + "/.local/rbsg/testArmies.json");
        if(localArmiesFile.exists()){
            localArmiesFile.delete();
        }
        Army army = new Army();
        ArrayList<Army> armies = new ArrayList<>();
        armies.add(army);
        persistantArmyManager.setTestFileName("testArmies.json");
        persistantArmyManager.saveArmiesLocal(armies);
        getArmiesService.setTestFileName("testArmies.json");

        try {
            List<Army> armyList = getArmiesService.loadLocalArmies();
            Assert.assertTrue(armyList.size()==1);
            Assert.assertTrue(armyList.get(0).units.size()==0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadArmiesLocal(){
        ArrayList<Army> armies = new ArrayList<>();
        persistantArmyManager.setTestFileName("testArmies.json");
        getArmiesService.setTestFileName("testArmies.json");

        for(int j = 0; j < 7; j++) {
            Army army = new Army();
            for (int i = 0; i < 10; i++) {
                Unit unit = new Unit();
                unit.id.set("5cc051bd62083600017db3b7");
                army.units.add(unit);
            }
            armies.add(army);
        }
        persistantArmyManager.saveArmiesLocal(armies);

        try {
            List<Army> armyList = getArmiesService.loadLocalArmies();
            Assert.assertTrue(armyList.size()==7);
            for(Army loadedArmy : armyList){
                Assert.assertTrue(loadedArmy.units.size()==10);
                for(Unit loadedUnit : loadedArmy.units){
                    Assert.assertTrue(loadedUnit.id.get()!="");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoading() throws ExecutionException, InterruptedException {
        ObservableList<Army> armies = FXCollections.observableArrayList();
        persistantArmyManager.setTestFileName("testArmies.json");
        getArmiesService.setTestFileName("testArmies.json");

        User user = new User("test123", "test123");
        user.setUserKey(
                loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );
        userProvider.set(user);

        //Cleanup environment
        for(Army army : getArmiesService.doQueryArmies()){
            deleteArmyService.deleteArmy(army);
        }

        for(int j = 0; j < 7; j++) {
            Army army = new Army();
            army.name.set("TestArmeeZumLaden");
            for (int i = 0; i < 10; i++) {
                Unit unit = new Unit();
                unit.id.set("5cc051bd62083600017db3b7");
                army.units.add(unit);
            }
            armies.add(army);
        }
        persistantArmyManager.saveArmies(armies);

        List<Army> mergedArmies = getArmiesService.loadArmies();

        Assert.assertTrue(mergedArmies.size()==7);

        try {
            List<Army> armyList = getArmiesService.loadLocalArmies();
            Assert.assertTrue(armyList.size()==7);
            for(Army loadedArmy : armyList){
                Assert.assertTrue(loadedArmy.units.size()==10);
                for(Unit loadedUnit : loadedArmy.units){
                    Assert.assertTrue(loadedUnit.id.get()!="");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMergingOnline() throws ExecutionException, InterruptedException {
        ObservableList<Army> armies = FXCollections.observableArrayList();
        persistantArmyManager.setTestFileName("testArmies.json");
        getArmiesService.setTestFileName("testArmies.json");

        User user = new User("test123", "test123");
        user.setUserKey(
                loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );
        userProvider.set(user);

        //Cleanup environment
        for(Army army : getArmiesService.doQueryArmies()){
            deleteArmyService.deleteArmy(army);
        }

        for(int j = 0; j < 7; j++) {
            Army army = new Army();
            army.name.set("TestArmeeZumLaden");
            for (int i = 0; i < 10; i++) {
                Unit unit = new Unit();
                unit.id.set("5cc051bd62083600017db3b7");
                army.units.add(unit);
            }
            armies.add(army);
        }
        persistantArmyManager.saveArmies(armies);
        armies.get(0).units.remove(0,2);
        ArrayList<Army> armyArrayList = new ArrayList<>();
        armyArrayList.addAll(armies);
        persistantArmyManager.saveArmiesLocal(armyArrayList);

        List<Army> mergedArmies = getArmiesService.loadArmies();

        Assert.assertTrue(mergedArmies.size()==7);

        try {
            List<Army> armyList = getArmiesService.loadLocalArmies();
            Assert.assertTrue(armyList.size()==7);
            Assert.assertTrue(armyList.get(0).units.size()==8);
            armyList.remove(armyList.get(0));
            for(Army loadedArmy : armyList){
                Assert.assertTrue(loadedArmy.units.size()==10);
                for(Unit loadedUnit : loadedArmy.units){
                    Assert.assertTrue(loadedUnit.id.get()!="");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
