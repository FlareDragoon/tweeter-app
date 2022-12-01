package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.user.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {
    private StatusService service;

    public FeedPresenter(View view) {
        this.view = view;
        service = new StatusService();
    }

    @Override
    public void loadMoreElements(User user) {
        setLoadingFlags(true);
        service.loadMoreFeed(Cache.getInstance().getCurrUserAuthToken(), user,
                PAGE_SIZE, lastElement, new StatusListObserver());
    }

    private class StatusListObserver extends ElementListObserver
            implements StatusService.GetStatusListObserver {
    }
}
