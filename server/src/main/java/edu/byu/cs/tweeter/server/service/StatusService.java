package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.StatusListTable;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.server.factory.FactoryInterface;

public class StatusService {
    private final FactoryInterface factory;


    public StatusService(FactoryInterface factory) {
        this.factory = factory;
    }

    public StoryResponse getStory(StoryRequest request) {
        if(request.getCurrUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getPagedStory(request);
    }

    public FeedResponse getFeed(FeedRequest request) {
        if(request.getCurrUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getPagedFeed(request);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
//        List<User> followers = getFollowDAO().getAllFollowers(request.getStatus().getUser());

        PostStatusResponse response = getStatusDAO().postStatus(request);
        pushToPostQueue(request.getStatus());

        return response;
    }

    private void pushToPostQueue(Status status) {
        Gson gson = new Gson();
        String messageBody = gson.toJson(status);

        String SQS_POST_URL = "https://sqs.us-west-2.amazonaws.com/375136350162/PostQueue";

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(SQS_POST_URL)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

        sqs.sendMessage(request);
    }

    public void writeToFeed(String body) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<StatusListTable>>(){}.getType();
        List<StatusListTable> statusListTables = gson.fromJson(body, listType);

        getStatusDAO().batchPost(statusListTables);
    }

    StatusDAOInterface getStatusDAO() { return factory.getStatusDAO(); }

    FollowDAOInterface getFollowDAO() {
        return factory.getFollowDAO();
    }


}
