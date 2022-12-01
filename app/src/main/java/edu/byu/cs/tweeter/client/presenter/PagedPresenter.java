package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.user.service.FollowService;
import edu.byu.cs.tweeter.client.user.service.UserService;
import edu.byu.cs.tweeter.client.user.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends BasePresenter {
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    protected T lastElement;

    protected static final int PAGE_SIZE = 10;

    protected View view;

    public interface View<U> extends BasePresenter.View {
        void setLoadingFooter(boolean value);
        void addElements(List<U> elements);
    }

    public final void setLoadingFlags(boolean value) {
        isLoading = value;
        view.setLoadingFooter(value);
    }

    public final void loadUser(User user) {
        view.displayInfoMessage("Getting user...");
        view.navigateToUser(user);
    }

    public final void getUser(AuthToken authToken, String username) {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, username, new GetUserObserver());
    }

    public final boolean hasMorePages() {
        return hasMorePages;
    }

    public final boolean isLoading() {
        return isLoading;
    }

    abstract public void loadMoreElements(User user);

    protected class GetUserObserver implements UserService.getUserObserver {
        @Override
        public void getUser(User user) {loadUser(user);}

        @Override
        public void handleFailure(String message) {
            failure(message, view);
        }

        @Override
        public void handleException(Exception exception) {
            String message = "Failed to login because of exception: " + exception.getMessage();
            failure(message, view);
        }
    }

    protected class ElementListObserver implements PageObserver<T> {
        @Override
        public final void handleFailure(String message) {
            setLoadingFlags(false);
            failure(message,view);
        }

        @Override
        public final void handleException(Exception e) {
            setLoadingFlags(false);
            String message = "Failed to get following because of exception: " + e.getMessage();
            failure(message,view);
        }

        @Override
        public final void addElements(List<T> elements, boolean hasMorePages) {
            setLoadingFlags(false);
            lastElement = (elements.size() > 0) ? elements.get(elements.size() - 1) : null;
            view.addElements(elements);
            PagedPresenter.this.hasMorePages = hasMorePages;
        }
    }
}
