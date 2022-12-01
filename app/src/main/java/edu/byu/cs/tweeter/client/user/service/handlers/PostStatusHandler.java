package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.user.service.StatusService;

// PostStatusHandler
public class PostStatusHandler extends BaseHandler<StatusService.PostObserver> {
    public PostStatusHandler(StatusService.PostObserver observer) {
        super(observer);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
        if (success) {
            observer.performAction("Successfully Posted!");
        } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
            observer.handleFailure("Failed to post status: " + message);
        } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
