package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowingRequest extends PagedRequest {
    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowingRequest() {}

    /**
     * Creates an instance.
     *
     * @param currUserAlias the alias of the user whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastListedAlias the alias of the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FollowingRequest(AuthToken authToken, String currUserAlias, int limit, String lastListedAlias) {
        this.authToken = authToken;
        this.currUserAlias = currUserAlias;
        this.limit = limit;
        this.lastListedAlias = lastListedAlias;
    }

}
