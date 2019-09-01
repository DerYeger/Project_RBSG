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
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.SaveFileStrategy;
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
        DeleteArmyService.class,
        SaveFileStrategy.class
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

    @Ignore
    @Test
    public void deleteArmyOnline() throws ExecutionException, InterruptedException {

        ArrayList<Army> armyList = null;
        ArrayList<Army> newArmyList = null;
        CompletableFuture<List<Army>> armyFuture;
        CompletableFuture<List<Army>> newArmyFuture;
        CompletableFuture<DeleteArmyResponse> response;


        User user = new User("test123", "test123");
        user.setUserKey(
                loginManager.callLogin(user).get().getBody().get("data").get("userKey").asText()
        );


        userProvider.set(user);
        armyFuture = getArmiesService.queryArmies();

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


        response = deleteArmyService.deleteArmy(armyList.get(0));


        while (!response.isDone()) {
            sleep(100);
        }


        Assert.assertTrue(response.get().status.equals("success"));


        newArmyFuture = getArmiesService.queryArmies();


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
        HttpEntity httpEntity = new HttpEntity(null);
        DeleteArmyResponse deleteArmyResponse = new DeleteArmyResponse();
        ResponseEntity onDeleteResponseEntity = new ResponseEntity(deleteArmyResponse, HttpStatus.ACCEPTED);
        CompletableFuture<DeleteArmyResponse> response;


        ArrayList<Army> armyList = new ArrayList<>();
        Army army = new Army();
        army.name.set("ggArmy");
        army.id.set("5d12111f2c945100017665d4");
        armyList.add(army);


        deleteArmyResponse.message = "Army deleted";
        deleteArmyResponse.status = "success";
        deleteArmyResponse.data = new DeleteArmyResponse.DeleteArmyResponseData();


        when(restMock.exchange(eq("/army/5d12111f2c945100017665d4"),
                eq(HttpMethod.DELETE),
                eq(httpEntity),
                eq(DeleteArmyResponse.class)))
                .thenReturn(onDeleteResponseEntity);


        response = deleteArmyService.deleteArmy(army);


        while (!response.isDone()) {
            sleep(100);
        }


        Assert.assertTrue(response.get().status.equals("success"));
        Assert.assertTrue(response.get().message.contains("Army deleted"));
    }
}
