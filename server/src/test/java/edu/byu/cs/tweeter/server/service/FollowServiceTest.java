package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.FollowDAO;

public class FollowServiceTest {

    private FollowerRequest request;
    private FollowerResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowService followServiceSpy;
    private User currentUser;

    @BeforeEach
    public void setup() {
        AuthToken authToken = new AuthToken();

        currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowerRequest(authToken, currentUser.getAlias(), 3, null);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
//        Mockito.when(mockFollowDAO.getFollowers(request)).thenReturn(expectedResponse);
//        Mockito.when(mockFollowDAO.getFollowerCount(currentUser)).thenReturn(20);

        followServiceSpy = Mockito.spy(FollowService.class);
        Mockito.when(followServiceSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    /**
     * Verify that the {@link FollowService#getFollowers(FollowerRequest)}
     * method returns the same result as the {@link FollowDAO} class.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() {
        FollowerResponse response = followServiceSpy.getFollowers(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFollowerCount_validRequest_correctResponse() {
//        Integer followerCount = followServiceSpy.getFollowerCount(currentUser);
//        Assertions.assertEquals(followerCount, 20);
    }
}
