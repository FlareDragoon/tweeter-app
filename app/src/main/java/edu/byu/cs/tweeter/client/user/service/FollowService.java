package edu.byu.cs.tweeter.client.user.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.user.service.handlers.FollowUnfollowHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.GetCountHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.IsFollowerHandler;
import edu.byu.cs.tweeter.client.user.service.handlers.PagedHandler;
import edu.byu.cs.tweeter.client.user.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.user.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {

    public interface GetUserListObserver extends PageObserver<User> {}

    public interface FollowObserver extends ServiceObserver {
        void updateSelectedUserFollowingAndFollowers();
        void setCount(int count, boolean followersCount);

        void displayInfoMessage(String message);

        void updateFollowButton(boolean value);
        void setButtonText(boolean value);
        void setEnabled(boolean value);
    }

    public void loadMoreFollowing(AuthToken currUserAuthToken, User user, int pageSize,
                                  User lastUser, GetUserListObserver followingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastUser, new PagedHandler<Status>(followingObserver));
        executeTask(getFollowingTask);
    }

    public void loadMoreFollowers(AuthToken currUserAuthToken, User user, int pageSize,
                              User lastUser, GetUserListObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastUser, new PagedHandler<Status>(observer));
        executeTask(getFollowersTask);

    }

    public void followUser(User user, AuthToken authToken, FollowObserver observer) {
        FollowTask followTask = new FollowTask(authToken, user,
                new FollowUnfollowHandler(observer, true));
        executeTask(followTask);
    }

    public void unfollowUser(AuthToken authToken, User selectedUser, FollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, selectedUser,
                new FollowUnfollowHandler(observer, false));
        executeTask(unfollowTask);
    }

    public void getFollowersCount(AuthToken authToken, User user, FollowObserver observer) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new GetCountHandler(observer, true));
        executeTask(followersCountTask);
    }

    public void getFollowingCount(AuthToken authToken, User user, FollowObserver observer) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new GetCountHandler(observer, false));
        executeTask(followingCountTask);
    }

    public void updateButtonText(AuthToken authToken, User user,
                                 User selectedUser, FollowObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, user,
                selectedUser, new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }
}
