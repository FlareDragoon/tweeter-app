package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.user.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BaseHandler<UserService.getUserObserver> {

    public GetUserHandler(UserService.getUserObserver observer) {
        super(observer);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
        if (success) {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
            observer.getUser(user);
        } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
            observer.handleFailure("Failed to get user's profile: " + message);
        } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
            observer.handleFailure("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }
}
