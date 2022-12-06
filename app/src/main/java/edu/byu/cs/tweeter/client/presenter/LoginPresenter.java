package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.user.service.UserService;
import edu.byu.cs.tweeter.client.user.service.observer.LoginAndRegisterObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends LoginAndRegisterPresenter implements LoginAndRegisterObserver {
    private UserService service;

    public LoginPresenter(View view) {
        this.view = view;
    }

    public UserService getUserService() {
        if (service == null) {
            service = new UserService();
        }
        return service;
    }

    public void initiateLogin(String username, String password) {
        String message = validateLogin(username, password);

        if (message == null) {
            displaySuccessMessage();
            getUserService().login(username, password, this);
        } else {
            failure(message, view);
        }
    }

    @Override
    public void loginOrRegisterUser(User user, AuthToken authToken) {
        loginOrRegister(user, authToken);
    }

    @Override
    public void handleFailure(String message) {
        failure(message, view);
    }

    @Override
    public void handleException(Exception exception) {
        String message = "Failed to login because of exception: " + exception.getMessage();
        failure(message, view);
    }
}
