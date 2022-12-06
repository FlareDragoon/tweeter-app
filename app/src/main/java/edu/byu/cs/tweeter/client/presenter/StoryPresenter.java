package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.user.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    private StatusService service;

    public StatusService getStatusService() {
        if (service == null) {
            service = new StatusService();
        }
        return service;
    }

    public StoryPresenter(View view) {
        this.view = view;
    }

    @Override
    public void loadMoreElements(User user) {
        setLoadingFlags(true);
        getStatusService().loadMoreStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                lastElement, new StatusListObserver());
    }

    private class StatusListObserver extends ElementListObserver
            implements StatusService.GetStatusListObserver {
    }
}
