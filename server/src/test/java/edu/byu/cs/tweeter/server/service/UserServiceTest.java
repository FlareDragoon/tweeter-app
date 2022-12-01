package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.UserDAO;

public class UserServiceTest {
    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private UserDAO mockUserDAO;
    private UserService userServiceSpy;

    @BeforeEach
    public void setup() {
        AuthToken authToken = new AuthToken();

        User currentUser = new User("John", "Smith", null);

        String username = "@uname";
        String password = "password";
        String firstName = "John";
        String lastName = "Smith";

        // Setup a request object to use in the tests
        request = new RegisterRequest(username, password,
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png",
                firstName, lastName);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new RegisterResponse(currentUser, authToken);
        mockUserDAO = Mockito.mock(UserDAO.class);
//        Mockito.when(mockUserDAO.register(request)).thenReturn(expectedResponse);

        userServiceSpy = Mockito.spy(UserService.class);
        Mockito.when(userServiceSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testRegister_validRequest_correctResponse() {
        RegisterResponse response = userServiceSpy.register(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
