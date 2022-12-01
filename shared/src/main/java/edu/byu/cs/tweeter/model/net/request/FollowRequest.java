package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    private User followee;
    private User follower;

    private FollowRequest() {}

    public FollowRequest(User followee, User follower) {
        this.follower = follower;
        this.followee = followee;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }
}
