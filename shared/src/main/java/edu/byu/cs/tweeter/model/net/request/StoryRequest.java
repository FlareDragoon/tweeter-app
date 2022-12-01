package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class StoryRequest extends PagedRequest {
    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private StoryRequest() {}

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose statuses are to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastTimeStamp the date of last post that was returned in the previous request (null if
     *                     there was no previous request or if no statuses were returned in the
     *                     previous request).
     */
    public StoryRequest(AuthToken authToken, String userAlias, int limit, String lastTimeStamp) {
        this.authToken = authToken;
        this.currUserAlias = userAlias;
        this.limit = limit;
        this.lastTimeStamp = lastTimeStamp;
    }
}
