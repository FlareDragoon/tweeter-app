package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.user.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User>{

    private FollowService service;

    public FollowingPresenter(View view) {
        this.view = view;
        service = new FollowService();
    }

    @Override
    public void loadMoreElements(User user) {
        setLoadingFlags(true);
        service.loadMoreFollowing(Cache.getInstance().getCurrUserAuthToken(), user,
                PAGE_SIZE, lastElement, new UserListObserver());
    }

    private class UserListObserver extends ElementListObserver
            implements FollowService.GetUserListObserver {}
}
