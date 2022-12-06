package edu.byu.cs.tweeter.client.backgroundTask;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenterUnitTest;
import edu.byu.cs.tweeter.client.user.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class PostStatusTaskTest {
    private Status status;
    private ServerFacade server;

    public void setUpTest(User user) throws IOException, TweeterRemoteException {
        status = new Status("Testing", user, "now", new ArrayList<>(), new ArrayList<>());
        server = new ServerFacade();
        PostStatusRequest request = new PostStatusRequest(status);

        testPostStatus(request);
    }

    public void testPostStatus(PostStatusRequest request) throws IOException, TweeterRemoteException {
        PostStatusResponse currResponse = server.postStatus(request, "/poststatus");
        Assertions.assertTrue(currResponse.isSuccess());

        MainPresenterUnitTest presenterTest = new MainPresenterUnitTest();

        // Test that the message is displayed to the user that the post was successful
        presenterTest.setup();
        presenterTest.testPost_Success();

        // Test that story retrieval works
        GetStoryTaksTest storyTest = new GetStoryTaksTest();
        storyTest.setUpStoryTest(status);

    }
}
