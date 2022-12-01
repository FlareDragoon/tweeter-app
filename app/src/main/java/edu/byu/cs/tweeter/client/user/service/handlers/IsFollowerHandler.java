package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.user.service.FollowService;

// IsFollowerHandler
public class IsFollowerHandler extends BaseHandler<FollowService.FollowObserver> {
    public IsFollowerHandler(FollowService.FollowObserver observer) {
        super(observer);
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
        if (success) {
            boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            // If logged in user if a follower of the selected user, display the follow button as "following"
            if (isFollower) {
                observer.setButtonText(true);
            } else {
                observer.setButtonText(false);
            }
        } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
            observer.displayInfoMessage("Failed to determine following relationship: " + message);
        } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
            observer.displayInfoMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }
}
