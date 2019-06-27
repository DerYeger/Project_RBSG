package de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.ServerConfig;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.ArmyAdapter;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.ArmyUnitAdapter;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetArmiesService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.serverResponses.DeleteArmyResponse;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.ApiClientErrorInterceptor;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.junit.Assert;
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

import java.util.ArrayList;
import java.util.List;
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
        ArmyAdapter.class,
        ObjectMapper.class,
        ArmyUnitAdapter.class,
        ApplicationState.class,
        GetArmiesService.class,
        DeleteArmyService.class
})


public class DeleteArmyTest {

    @Autowired
    GetArmiesService getArmiesService;
    @Autowired
    DeleteArmyService deleteArmyService;
    @Autowired
    LoginManager loginManager;
    @Autowired
    UserProvider userProvider;
    @Autowired
    ArmyAdapter armyAdapter;
    @Autowired
    RestTemplate rbsgTemplate;

    @Test
    public void deleteArmyOnline() throws ExecutionException, InterruptedException {

        User user = new User("test123", "test123");
        user.setUserKey(
                loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );
        userProvider.set(user);

        CompletableFuture<List<Army>> armyFuture = getArmiesService.queryArmies();
        ArrayList<Army> armyList = null;
        try {
            armyList = (ArrayList<Army>) armyFuture.get();
            if (armyList.size() == 0) {
                System.out.println("No Army on the server to test.");
                Assert.assertTrue(false);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        CompletableFuture<DeleteArmyResponse> response = deleteArmyService.deleteArmy(armyList.get(0));
        while (!response.isDone()) {
            sleep(100);
        }
        Assert.assertTrue(response.get().status.equals("success"));


        CompletableFuture<List<Army>> newArmyFuture = getArmiesService.queryArmies();
        ArrayList<Army> newArmyList = null;
        try {
            newArmyList = (ArrayList<Army>) newArmyFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(newArmyList.size() == armyList.size() - 1);
        Assert.assertTrue(!newArmyList.contains(armyList.get(0)));

        for (Army army : newArmyList) {
            Assert.assertTrue(army.id.get() != armyList.get(0).id.get());
        }
    }

    @Test
    public void deleteArmyLocal() throws InterruptedException, ExecutionException {
        RestTemplate restMock = mock(RestTemplate.class);
        DeleteArmyService deleteArmyService = new DeleteArmyService(restMock);
        DeleteArmyResponse deleteArmyResponse = new DeleteArmyResponse();
        ArrayList<Army> armyList = new ArrayList<>();
        HttpEntity httpEntity = new HttpEntity(null);

        ResponseEntity onDeleteResponseEntity = new ResponseEntity(deleteArmyResponse, HttpStatus.ACCEPTED);
        Army army = new Army();
        army.name.set("ggArmy");
        army.id.set("5d12111f2c945100017665d4");
        armyList.add(army);

        deleteArmyResponse.message = "Army deleted";
        deleteArmyResponse.status = "success";
        deleteArmyResponse.data = new DeleteArmyResponse.DeleteArmyResponseData();

        when(restMock.exchange(eq("/army/5d12111f2c945100017665d4"), eq(HttpMethod.DELETE), eq(httpEntity), eq(DeleteArmyResponse.class)))
                .thenReturn(onDeleteResponseEntity);

        CompletableFuture<DeleteArmyResponse> response = deleteArmyService.deleteArmy(army);
        while (!response.isDone()) {
            sleep(100);
        }

        Assert.assertTrue(response.get().status.equals("success"));
        Assert.assertTrue(response.get().message.contains("Army deleted"));
    }
}
