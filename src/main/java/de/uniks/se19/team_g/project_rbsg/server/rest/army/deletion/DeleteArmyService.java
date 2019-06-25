package de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeleteArmyService {

    private final RestTemplate rbsgTemplate;
    private final String URL = "/army/";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DeleteArmyService(@NonNull RestTemplate restTemplate) {

        this.rbsgTemplate = restTemplate;
    }

    public void deleteArmy(Army army) {

        String deleteArmyUrl = URL + army.id.get();

        //No return type, exchange doesnt work with HTTPRequest.DELETE
        rbsgTemplate.delete(deleteArmyUrl);

        //ToDO: Auto-Save after deletion. Otherwise local and remote state are diverged.
    }
}
