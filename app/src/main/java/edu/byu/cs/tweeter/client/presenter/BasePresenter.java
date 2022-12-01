package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class BasePresenter {
    public interface View {
        void displayInfoMessage(String message);
        void clearInfoMessage();

        void clearErrorMessage();

        void navigateToUser(User user);
    }

    public final void failure(String message, View view) {
        view.displayInfoMessage(message);
    }
}