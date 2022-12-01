package edu.byu.cs.tweeter.client.user.service;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.user.service.handlers.PagedHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.PostStatusHandler;
import edu.byu.cs.tweeter.client.user.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.user.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends BaseService {

    public interface GetStatusListObserver extends PageObserver<Status> {
    }

    public interface PostObserver extends ServiceObserver {
        void performAction(String message);
    }

    public void postStatus(AuthToken authToken, Status newStatus, PostObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken,
                newStatus, new PostStatusHandler(observer));
        executeTask(statusTask);
    }

    public void loadMoreFeed(AuthToken currUserAuthToken, User user, int pageSize,
                             Status lastStatus, GetStatusListObserver statusObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken, user, pageSize,
                lastStatus, new PagedHandler<Status>(statusObserver));
        executeTask(getFeedTask);
    }

    public void loadMoreStory(AuthToken currUserAuthToken, User user, int pageSize,
                              Status lastStatus, GetStatusListObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize,
                lastStatus, new PagedHandler<Status>(observer));
        executeTask(getStoryTask);
    }

}
