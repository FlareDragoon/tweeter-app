package edu.byu.cs.tweeter.client.user.service.handlers;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.user.service.FollowService;

// Follow and Unfollow Handler
public class FollowUnfollowHandler extends BaseHandler<FollowService.FollowObserver> {
    private boolean value;
    public FollowUnfollowHandler(FollowService.FollowObserver observer, boolean value) {
        super(observer);
        this.value = value;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
        if (success) {
            observer.updateSelectedUserFollowingAndFollowers();
            observer.updateFollowButton(value);
        } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
            observer.displayInfoMessage("Failed to follow: " + message);
        } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
            observer.displayInfoMessage("Failed to follow because of exception: " + ex.getMessage());
        }

        observer.setEnabled(true);
    }
}