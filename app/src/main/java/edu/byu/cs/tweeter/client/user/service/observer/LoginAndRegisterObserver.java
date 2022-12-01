package edu.byu.cs.tweeter.client.user.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface LoginAndRegisterObserver extends ServiceObserver {
    void loginOrRegisterUser(User user, AuthToken authToken);
}
