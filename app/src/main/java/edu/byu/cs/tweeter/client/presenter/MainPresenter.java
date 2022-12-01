package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.user.service.FollowService;
import edu.byu.cs.tweeter.client.user.service.StatusService;
import edu.byu.cs.tweeter.client.user.service.UserService;
import edu.byu.cs.tweeter.client.user.service.observer.FollowObserver;
import edu.byu.cs.tweeter.client.user.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.user.service.observer.StatusObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private View view;
    private FollowService followService;
    private StatusService statusService;
    private UserService userService;

    private User selectedUser = null;

    public MainPresenter(View view) {
        this.view = view;
    }

    public StatusObserver getStatusObserver() {
        return new StatusObserver(view);
    }

    public LogoutObserver getLogoutObserver() {
        return new LogoutObserver(view);
    }

    public FollowObserver getFollowObserver(User selectedUser) {
        return  new FollowObserver(view, selectedUser);
    }

    protected FollowService getFollowService() {
        if (followService == null) {
            followService = new FollowService();
        }
        return followService;
    }

    protected UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public interface View {
        void displayMessage(String message);
        void clearInfoMessage();

        void updateFollowButton(boolean value);
        void updateFollowerCount(int count);
        void updateFolloweeCount(int count);

        void updateButtonText(boolean value);

        void postSuccess();

        void logoutUser();

        void enableButton(boolean value);
    }

    public  void initiateLogout() {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        getUserService().logout(authToken,getLogoutObserver());
    }

    public void initiatePost(String post) {
        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(),
                    getFormattedDateTime(), parseURLs(post), parseMentions(post));

            AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
            getStatusService().postStatus(authToken,newStatus, getStatusObserver());

        } catch (Exception ex) {
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);
                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }
        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }
        return containedMentions;
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() +
                " " + LocalTime.now().toString().substring(0, 8)));
    }

    public void updateButtonText(User selectedUser) {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        this.selectedUser = selectedUser;
        User user = Cache.getInstance().getCurrUser();
        getFollowService().updateButtonText(authToken, user, this.selectedUser, getFollowObserver(selectedUser));
    }

    public void initiateFollow(User user) {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        selectedUser = user;
        getFollowService().followUser(user, authToken, getFollowObserver(selectedUser));
    }

    public void initiateUnfollow(User user) {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        selectedUser = user;
        getFollowService().unfollowUser(authToken, selectedUser, getFollowObserver(selectedUser));
    }
}
