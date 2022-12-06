package edu.byu.cs.tweeter.client.backgroundTask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenterUnitTest;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class LoginTaskTest {
    private LoginResponse expectedResponse;
    private LoginRequest request;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        AuthToken token = new AuthToken();
        User user = new User("john", "smith", "@john", null);
        expectedResponse = new LoginResponse(user, token);

        request = new LoginRequest("@john", "john");
    }

    @Test
    public void login_post_getStory_Test() throws IOException, TweeterRemoteException {
        ServerFacade server = new ServerFacade();
        LoginResponse currResponse = server.login(request, "/login");

        Assertions.assertNotNull(currResponse);
        Assertions.assertEquals(currResponse.getUser(), expectedResponse.getUser());

        PostStatusTaskTest postStatusTest = new PostStatusTaskTest();
        postStatusTest.setUpTest(currResponse.getUser());
    }

}
