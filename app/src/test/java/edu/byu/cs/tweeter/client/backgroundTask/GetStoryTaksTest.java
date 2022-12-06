package edu.byu.cs.tweeter.client.backgroundTask;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class GetStoryTaksTest {
    private Status status;
    private ServerFacade server;
    private StoryResponse expectedResponse;
    private List<Status> story;

    public void setUpStoryTest(Status status) throws IOException, TweeterRemoteException {
        this.status = status;

//        expectedResponse = new StoryResponse(story, false);
        server = new ServerFacade();

        StoryRequest request = new StoryRequest(null, "@john", 10, "");

        checkStory(request);
    }

    public void checkStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryResponse currResponse = server.getStory(request, "/getstory");

        story = currResponse.getStory();

        System.out.println(story.get(0).toString());

//        Assertions.assertEquals(expectedResponse, currResponse);
        Assertions.assertEquals(status, story.get(0));
    }
}
