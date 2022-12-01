package edu.byu.cs.tweeter.client.user.service.observer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.user.service.UserService;

public class LogoutObserver implements  UserService.logoutObserver {
    private MainPresenter.View view;
    public LogoutObserver(MainPresenter.View view) {
        this.view = view;
    }

    @Override
    public void logout() {
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        view.clearInfoMessage();
        view.logoutUser();
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage(message);
    }

    @Override
    public void handleException(Exception exception) {
        view.displayMessage("Ran into exception: " + exception.getMessage());

    }
}
