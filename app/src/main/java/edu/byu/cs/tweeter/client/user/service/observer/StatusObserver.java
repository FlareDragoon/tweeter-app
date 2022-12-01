package edu.byu.cs.tweeter.client.user.service.observer;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.user.service.StatusService;

public class StatusObserver implements StatusService.PostObserver {
    private MainPresenter.View view;
    public StatusObserver(MainPresenter.View view) {
        this.view = view;
    }
    @Override
    public void performAction(String message) {
        view.postSuccess();
        view.displayMessage(message);
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage(message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
    }
}
