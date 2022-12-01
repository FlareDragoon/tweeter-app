package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class PagedRequest {
    protected AuthToken authToken;
    protected String currUserAlias;
    protected int limit;
    protected String lastTimeStamp;
    protected String lastListedAlias;


    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getCurrUserAlias() {
        return currUserAlias;
    }

    /**
     * Sets the follower.
     *
     * @param currUserAlias the follower.
     */
    public void setCurrUserAlias(String currUserAlias) {
        this.currUserAlias = currUserAlias;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last status that was returned in the previous request or null if there was no
     * previous request or if no status were returned in the previous request.
     *
     * @return the last status.
     */
    public String getLastTimeStamp() {
        return lastTimeStamp;
    }

    /**
     * Sets the last status.
     *
     * @param lastTimeStamp the last status.
     */
    public void setLastTimeStamp(String lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public String getLastListedAlias() {
        return lastListedAlias;
    }

    /**
     * Sets the last followee.
     *
     * @param lastListedAlias the last followee.
     */
    public void setLastListedAlias(String lastListedAlias) {
        this.lastListedAlias = lastListedAlias;
    }
}
