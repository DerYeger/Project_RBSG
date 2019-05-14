package de.uniks.se19.team_g.project_rbsg;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Model.User;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RegistrationManagerTest {

    @Test
    public void onRegistrationTest(){
        User testUser = new User("Keanu", "geheim");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("json/testPerson.json"), testUser);
        } catch (IOException e){
            e.printStackTrace();
        }
        RegistrationManager registrationManager = new RegistrationManager(new TestClient());
        registrationManager.onRegistration(testUser);


    }
}
