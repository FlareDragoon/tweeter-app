package edu.byu.cs.tweeter.server.dao.DummyDAO;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusDAO {

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public StoryResponse getStory(StoryRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getCurrUserAlias() != null;

        List<Status> allPosts = getDummyStatuses();
        List<Status> responseStories = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allPosts != null) {
                int storiesIndex = getStartingIndex(request.getLastTimeStamp(), allPosts);

                for(int limitCounter = 0; storiesIndex < allPosts.size() && limitCounter < request.getLimit(); storiesIndex++, limitCounter++) {
                    responseStories.add(allPosts.get(storiesIndex));
                }

                hasMorePages = storiesIndex < allPosts.size();
            }
        }

        return new StoryResponse(responseStories, hasMorePages);
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FeedResponse getFeed(FeedRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getCurrUserAlias() != null;

        List<Status> allPosts = getDummyStatuses();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allPosts != null) {
                int feedIndex = getStartingIndex(request.getLastTimeStamp(), allPosts);

                for(int limitCounter = 0; feedIndex < allPosts.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
                    responseFeed.add(allPosts.get(feedIndex));
                }

                hasMorePages = feedIndex < allPosts.size();
            }
        }

        return new FeedResponse(responseFeed, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastStatusMessage the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allStatuses the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getStartingIndex(String lastStatusMessage, List<Status> allStatuses) {

        int currIndex = 0;

        if(lastStatusMessage != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatusMessage.equals(allStatuses.get(i).getPost())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    currIndex = i + 1;
                    break;
                }
            }
        }

        return currIndex;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        assert request.getStatus() != null;

        //TODO implement in M4
        return new PostStatusResponse();
    }

    /**
     * Returns the list of dummy story data. This is written as a separate method to allow
     * mocking of the story.
     *
     * @return the story.
     */
    final List<Status> getDummyStatuses() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    final FakeData getFakeData() {
        return FakeData.getInstance();
    }

}
