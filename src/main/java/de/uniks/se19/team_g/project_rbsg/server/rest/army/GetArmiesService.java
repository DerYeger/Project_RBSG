package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class GetArmiesService {

    private final RestTemplate rbsgTemplate;

    public GetArmiesService(RestTemplate rbsgTemplate) {
        this.rbsgTemplate = rbsgTemplate;
    }

    public List queryArmies() {
        final Response response = rbsgTemplate.getForObject("/army", Response.class);

        return response.data;
    }

    public static class Response extends RBSGDataResponse<List<Response.Data>> {
        public static class Data {
            public String id;
            public String name;
            public List<String> units;
        }
    }

}
