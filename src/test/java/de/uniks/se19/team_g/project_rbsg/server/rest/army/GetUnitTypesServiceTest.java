package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.ServerConfig;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetUnitTypesService.ResponseType;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServerConfig.class,
        LoginManager.class,
        UserProvider.class,
        GetUnitTypesService.class,
        UserKeyInterceptor.class,
})
public class GetUnitTypesServiceTest {

    @Autowired
    public LoginManager loginManager;
    @Autowired
    public RestTemplate rbsgTemplate;
    @Autowired
    public UserProvider userProvider;
    @Autowired
    public GetUnitTypesService service;

    @Test
    @Ignore
    public void queryUnitTypesOnline() throws ExecutionException, InterruptedException {
        User user = new User("ggEngineering", "ggEngineering");
        user.setUserKey(
            loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );
        userProvider.set(user);

        final List<Unit> unitTypes = service.queryUnitPrototypes().get();

        Assert.assertTrue(unitTypes.size() > 0);
    }

    @Test
    public void queryUnitPrototypes() throws ExecutionException, InterruptedException {
        UnitType type1 = new UnitType();
        UnitType type2 = new UnitType();
        ResponseType response = new ResponseType();
        response.data = Arrays.asList(type1, type2);

        Unit unit1 = new Unit();
        Unit unit2 = new Unit();

        RestTemplate restMock = mock(RestTemplate.class);
        when(restMock.getForObject("/army/units", ResponseType.class))
            .thenReturn(response);

        UnitTypeAdapter adapterMock = mock(UnitTypeAdapter.class);
        when(adapterMock.map(type1)).thenReturn(unit1);
        when(adapterMock.map(type2)).thenReturn(unit2);

        InOrder inOrder = inOrder(restMock, adapterMock);

        GetUnitTypesService sut = new GetUnitTypesService(
            restMock,
            adapterMock
        );

        final List<Unit> units = sut.queryUnitPrototypes().get();

        Assert.assertEquals(2, units.size());
        Assert.assertSame( unit1, units.get(0));
        Assert.assertSame( unit2, units.get(1));
        inOrder.verify(restMock).getForObject("/army/units", ResponseType.class);
        inOrder.verify(adapterMock).map(type1);
        inOrder.verify(adapterMock).map(type2);
    }
}