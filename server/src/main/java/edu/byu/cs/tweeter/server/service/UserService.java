package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.UserDAO;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.factory.FactoryInterface;

public class UserService {
    private final FactoryInterface factory;

    public UserService(FactoryInterface factory) {
        this.factory = factory;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse response;
        try {
            response = getUserDAO().login(request);
            if (request.getUsername() == null) {
                throw new RuntimeException("[Bad Request] Missing a username");
            } else if (request.getPassword() == null) {
                throw new RuntimeException("[Bad Request] Missing a password");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new LoginResponse(ex.getMessage());
        }

        return response;
    }

    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse response;

        try {
            if(request.getUsername() == null){
                throw new RuntimeException("[Bad Request] Missing a username");
            } else if(request.getPassword() == null) {
                throw new RuntimeException("[Bad Request] Missing a password");
            } else if(request.getImageBytesBase64() == null) {
                throw new RuntimeException("[Bad Request] Missing a photo");
            } else if(request.getFirstName() == null) {
                throw new RuntimeException("[Bad Request] Missing a first name");
            } else if(request.getLastName() == null) {
                throw new RuntimeException("[Bad Request] Missing a last name");
            }
            response = getUserDAO().register(request);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return new RegisterResponse(ex.getMessage());
        }

        return response;
    }

    public GetUserResponse getUser(GetUserRequest request) {
        GetUserResponse response;
        try {
            if (request.getAlias() == null) {
                throw new RuntimeException("[Bad Request] Missing a username");
            }
            response = getUserDAO().getUser(request);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new GetUserResponse(ex.getMessage());
        }

        return response;
    }

    public LogoutResponse logout(LogoutRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authToken");
        }
        return getUserDAO().logout(request);
    }

    UserDAOInterface getUserDAO() {return factory.getUserDAO();}
}
