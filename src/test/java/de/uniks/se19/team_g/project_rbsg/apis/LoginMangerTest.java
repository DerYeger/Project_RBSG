package de.uniks.se19.team_g.project_rbsg.apis;

import com.mashape.unirest.http.*;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;
import de.uniks.se19.team_g.project_rbsg.view.LoginFormBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import org.apache.http.HttpResponseFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Keanu Stückrad
 */
@Component
public class LoginMangerTest extends ApplicationTest {

    private LoginManager loginManager;

    @Autowired
    private ApplicationContext context;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final LoginFormBuilder loginFormBuilder = context.getBean(LoginFormBuilder.class);
        final Node login = loginFormBuilder.getLoginForm();
        Assert.assertNotNull(login);

        final Scene scene = new Scene((Parent) login);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void loginTest() throws JSONException {

        //loginManager = setLoginManager("success", "", (JsonObject) Json.createObjectBuilder().add("userKey", "ohYesYoureSuchAGodDamnKey"));

        final TextInputControl nameInput = lookup("#name-field").queryTextInputControl();
        Assert.assertNotNull(nameInput);
        final TextInputControl passwordInput = lookup("#password-field").queryTextInputControl();
        Assert.assertNotNull(passwordInput);
        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(nameInput);
        write("MasterChief");

        clickOn(passwordInput);
        write("john-117");

        clickOn(loginButton);
    }

    @Test
    public void loginTestFailureInvalidCredentialsAlert() throws JSONException {

        //loginManager = setLoginManager("failure", "Invalid credentials", (JsonObject) Json.createObjectBuilder());

        final TextInputControl nameInput = lookup("#name-field").queryTextInputControl();
        Assert.assertNotNull(nameInput);
        final TextInputControl passwordInput = lookup("#password-field").queryTextInputControl();
        Assert.assertNotNull(passwordInput);
        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(nameInput);
        write("WrongName");

        clickOn(passwordInput);
        write("thisIsNotARightPassword");

        clickOn(loginButton);
    }

    @Test
    public void loginTestFailureNoConnection() throws JSONException, ExecutionException, InterruptedException {

        /*
        loginManager = setLoginManager("failure", "No server connection", Json.createObjectBuilder().build());
        JsonObject body = Json.createObjectBuilder()
                .add("name", "cool")
                .add("password", "man")
                .build();
        HttpResponse<JsonNode> response = loginManager.baseRequest.asJsonAsync().get();
        JsonNode jsonNode = response.getBody();
        String str = jsonNode.toString();
        Assert.assertEquals("failure", str);
        */

        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(loginButton);
    }

    /*
    private LoginManager setLoginManager(@NonNull final String status, @NonNull final String message, @NonNull final JsonObject data){
        LoginManager loginManager = new LoginManager(new BaseRequest(){
            JsonObject body = Json.createObjectBuilder()
                    .add("status", status)
                    .add("message", message)
                    .add("data", data)
                    .build();
            JsonNode json = new JsonNode(body.toString());


            HttpResponse<JsonNode> response = new HttpResponse<JsonNode>((org.apache.http.HttpResponse) json,JsonNode.class);

            @Override
            public Future<HttpResponse<JsonNode>> asJsonAsync() {
                return new Future<HttpResponse<JsonNode>>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false; // not important
                    }
                    public boolean isCancelled() {
                        return false; // not important
                    }
                    public boolean isDone() {
                        return false; // not important
                    }
                    public HttpResponse<JsonNode> get() throws InterruptedException, ExecutionException {
                        return response;
                    }
                    public HttpResponse<JsonNode> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null; // not important
                    }
                };
            }
        });

        return loginManager;
    }
    */

}
