package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class RegistrationManagerTest {

    @Test
    public void onRegistrationTest(){
        User testUser = new User("Keanu", "geheim");
        RegistrationManager registrationManager = new RegistrationManager();
        boolean registrationAnswer = registrationManager.onRegistration(testUser);
        Assert.assertTrue(registrationAnswer);

    }
}
