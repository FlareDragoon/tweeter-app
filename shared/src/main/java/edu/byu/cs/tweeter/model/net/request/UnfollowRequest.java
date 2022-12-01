package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest {
    private User followee;
    private User follower;

    private UnfollowRequest() {}

    public UnfollowRequest(User followee, User follower) {
        this.followee = followee;
        this.follower = follower;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }
}
