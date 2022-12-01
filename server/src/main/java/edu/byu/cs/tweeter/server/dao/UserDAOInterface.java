package edu.byu.cs.tweeter.server.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public interface UserDAOInterface {
    LoginResponse login(LoginRequest request) throws DynamoDbException,
            ParseException, NoSuchAlgorithmException, InvalidKeySpecException;

    RegisterResponse register(RegisterRequest request) throws DynamoDbException,
            ParseException, NoSuchAlgorithmException, InvalidKeySpecException;

    GetUserResponse getUser(GetUserRequest request);

    LogoutResponse logout(LogoutRequest request);

    void updateCount(User user, boolean incrementCount, boolean isFollower);

    GetCountResponse getCount(User user, boolean isFollowingCount);
}
