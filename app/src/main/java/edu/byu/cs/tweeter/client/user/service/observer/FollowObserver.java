package edu.byu.cs.tweeter.client.user.service.observer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.user.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowObserver implements FollowService.FollowObserver{
    private MainPresenter.View view;
    private User selectedUser;

    public FollowObserver(MainPresenter.View view, User selectedUser) {
        this.view = view;
        this.selectedUser = selectedUser;
    }
    @Override
    public void updateSelectedUserFollowingAndFollowers() {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        new FollowService().getFollowersCount(authToken, selectedUser, this);
        new FollowService().getFollowingCount(authToken, selectedUser, this);
    }

    @Override
    public void updateFollowButton(boolean value) {
        view.updateFollowButton(value);
    }

    @Override
    public void setEnabled(boolean value) {
        view.enableButton(value);
    }
    @Override
    public void displayInfoMessage(String message) {
        view.displayMessage(message);
    }

    @Override
    public void setCount(int count, boolean followersCount) {
        if (followersCount) {
            view.updateFollowerCount(count);
        } else {
            view.updateFolloweeCount(count);
        }
    }

    @Override
    public void setButtonText(boolean value) {
        view.updateButtonText(value);
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