package edu.byu.cs.tweeter.client.user.service;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.user.service.handlers.GetUserHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.IsFollowerHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.LoginRegisterHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.LogoutHandler;
import edu.byu.cs.tweeter.client.user.service.observer.LoginAndRegisterObserver;
import edu.byu.cs.tweeter.client.user.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends BaseService {

    public interface logoutObserver extends ServiceObserver {
        void logout();
    }
    public interface getUserObserver extends ServiceObserver {
        void getUser(User user);
    }

    public void login(String username, String password, LoginAndRegisterObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new LoginRegisterHandler(observer));
        executeTask(loginTask);
    }

    public void register(String firstName, String lastName, String username,
                         String password, String imageBytesBase64, LoginAndRegisterObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username,
                password, imageBytesBase64, new LoginRegisterHandler(observer));
        executeTask(registerTask);
    }

    public void getUser(AuthToken authToken, String username, getUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, username,
                new GetUserHandler(observer));
        executeTask(getUserTask);
    }

    public void logout(AuthToken token, logoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(token, new LogoutHandler(observer));
        executeTask(logoutTask);
    }
}
