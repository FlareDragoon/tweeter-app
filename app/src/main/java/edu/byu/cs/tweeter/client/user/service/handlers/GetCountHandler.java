package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.user.service.FollowService;

// GetCountHandler
public class GetCountHandler extends BaseHandler<FollowService.FollowObserver> {
    private boolean value;
    public GetCountHandler(FollowService.FollowObserver observer, boolean value) {
        super(observer);
        this.value = value;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetCountTask.SUCCESS_KEY);
        if (success) {
            int count = msg.getData().getInt(GetCountTask.COUNT_KEY);
            observer.setCount(count, value);
        } else if (msg.getData().containsKey(GetCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetCountTask.MESSAGE_KEY);
            observer.displayInfoMessage("Failed to get count: " + message);
        } else if (msg.getData().containsKey(GetCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetCountTask.EXCEPTION_KEY);
            observer.displayInfoMessage("Failed to get count because of exception: " + ex.getMessage());
        }
    }
}
