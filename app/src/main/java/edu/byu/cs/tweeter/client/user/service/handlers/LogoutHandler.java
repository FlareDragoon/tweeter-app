package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.user.service.UserService;

// LogoutHandler
public class LogoutHandler extends BaseHandler<UserService.logoutObserver> {

    public LogoutHandler(UserService.logoutObserver observer) {
        super(observer);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
        if (success) {
            observer.logout();
        } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
            observer.handleFailure("Failed to logout because of exception: " + ex.getMessage());
        }
    }
}
