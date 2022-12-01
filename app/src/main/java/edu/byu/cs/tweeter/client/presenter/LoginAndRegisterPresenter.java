package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class LoginAndRegisterPresenter extends BasePresenter {
    protected View view;

    public interface View extends BasePresenter.View {
        void displayErrorMessage(String message);
    }

    public final String validateLogin(String username, String password) {
        if (username.length() == 0) {
            return ("Username cannot be empty.");
        }
        if (username.charAt(0) != '@') {
            return ("Alias must begin with @.");
        }
        if (username.length() < 2) {
            return ("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            return ("Password cannot be empty.");
        }
        return null;
    }

    public final void loginOrRegister(User user, AuthToken authToken){
        // Cache user session information
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        if (user == null) {
            view.displayInfoMessage("User is null");
            return;
        }
        view.clearInfoMessage();
        view.clearErrorMessage();

        view.displayInfoMessage("Hello " + user.getFirstName());
        view.navigateToUser(user);
    }

    public final void displaySuccessMessage() {
        view.clearErrorMessage();
        view.displayInfoMessage("Logging in...");
    }

}
