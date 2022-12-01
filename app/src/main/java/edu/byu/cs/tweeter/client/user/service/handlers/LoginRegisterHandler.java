package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.user.service.observer.LoginAndRegisterObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginRegisterHandler extends BaseHandler<LoginAndRegisterObserver> {
    public LoginRegisterHandler(LoginAndRegisterObserver observer) {
        super(observer);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
        if (success) {
            User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

            observer.loginOrRegisterUser(loggedInUser, authToken);

        } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
            observer.handleFailure("Failed to login: " + message);

        } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}