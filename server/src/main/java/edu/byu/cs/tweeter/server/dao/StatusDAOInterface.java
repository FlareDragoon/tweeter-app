package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.StatusListTable;

public interface StatusDAOInterface {
    PostStatusResponse postStatus(PostStatusRequest request);

    PostStatusResponse postStatus(PostStatusRequest request, List<User> followers);

    StoryResponse getPagedStory(StoryRequest storyRequest);

    FeedResponse getPagedFeed(FeedRequest feedRequest);

    void batchPost(List<StatusListTable> statusListTables);
}
