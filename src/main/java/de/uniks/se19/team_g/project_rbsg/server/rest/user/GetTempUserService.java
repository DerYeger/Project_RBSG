package de.uniks.se19.team_g.project_rbsg.server.rest.user;

import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Component
public class GetTempUserService implements Supplier<User> {

    final private RestTemplate rbsgTemplate;
    private final TempUserAdapter adapter;

    public GetTempUserService(
            RestTemplate rbsgTemplate,
            TempUserAdapter adapter
    ) {
        this.rbsgTemplate = rbsgTemplate;
        this.adapter = adapter;
    }

    @Override
    public User get() {
        Response response = rbsgTemplate.getForObject("/user/temp", Response.class);
        if (response == null) {
            return null;
        }
        return adapter.adapt(response.data);
    }

    static class Response extends RBSGDataResponse<TempUserData> {}

    public static class TempUserData {
        public String name;
        public String password;
    }

    @Component
    public static class TempUserAdapter {

        public User adapt(TempUserData data) {
            return new User(data.name, data.password);
        }
    }
}
