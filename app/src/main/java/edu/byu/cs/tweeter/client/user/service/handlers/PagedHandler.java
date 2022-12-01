package edu.byu.cs.tweeter.client.user.service.handlers;

import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.user.service.observer.PageObserver;

public class PagedHandler<T> extends BaseHandler<PageObserver> {

    public PagedHandler(PageObserver observer) {
        super(observer);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
        if (success) {
            List<T> elements = (List<T>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.addElements(elements, hasMorePages);
        } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
            observer.handleFailure("Failed to get feed: " + message);
        } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }


}
