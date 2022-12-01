package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {
    private boolean isFollowing;

    public IsFollowerResponse(boolean isFollowing) {
        super(true, null);
        this.isFollowing = isFollowing;
    }

    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
