package edu.byu.cs.tweeter.server.dao.DummyDAO;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class UserDAO {

    public LoginResponse login(LoginRequest request) {
        assert request.getUsername() != null;
        assert request.getPassword() != null;

        //TODO implement in M4
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();

        return new LoginResponse(user, authToken);

    }

    public RegisterResponse register(RegisterRequest request) {
        assert request.getFirstName() != null;
        assert request.getLastName() != null;
        assert request.getImageBytesBase64() != null;
        assert request.getUsername() != null;
        assert request.getPassword() != null;

        //TODO implement in M4
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();

        return new RegisterResponse(user, authToken);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        assert request.getAlias() != null;

        //TODO implement in M4
        User user = getUserByAlias(request.getAlias());

        return new GetUserResponse(user);
    }

    public LogoutResponse logout(LogoutRequest request) {
        assert request.getAuthToken() != null;

        //TODO implement in M4
        return new LogoutResponse();
    }

    private User getUserByAlias(String alias) {
        return getFakeData().findUserByAlias(alias);
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

}
