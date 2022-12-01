package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.StatusListTable;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.FollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.factory.FactoryInterface;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    private final FactoryInterface factory;

    public FollowService(FactoryInterface factory) {
        this.factory = factory;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getCurrUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowDAO().getPagedFollowees(request);
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        if(request.getCurrUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowDAO().getPagedFollowers(request);
    }

    public FollowResponse followUser(FollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        FollowResponse output =  getFollowDAO().followUser(request);

        if (output.isSuccess()) {
            getUserDAO().updateCount(request.getFollower(), true, false);
            getUserDAO().updateCount(request.getFollowee(), true, true);
        }
        return output;
    }

    public UnfollowResponse unfollowUser(UnfollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        UnfollowResponse output =  getFollowDAO().unfollowUser(request);

        if (output.isSuccess()) {
            getUserDAO().updateCount(request.getFollower(), false, false);
            getUserDAO().updateCount(request.getFollowee(), false, true);
        }
        return output;
    }

    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        IsFollowerResponse response = getFollowDAO().isFollowing(request);
        System.out.println("isFollower response value: " + response.isFollowing());
        return response;
    }

    public GetCountResponse getFollowingCount(GetCountRequest request) {
        if(request == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }
        User user = request.getUser();
        return getUserDAO().getCount(user, true);
    }

    public GetCountResponse getFollowerCount(GetCountRequest request) {
        if(request == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }
        User user = request.getUser();
        System.out.println(user.getAlias());
        return getUserDAO().getCount(user, false);
    }

    public void fetchFollowers(String body) {
        int PAGE_SIZE = 25;
        Gson gson = new Gson();

        Status status = gson.fromJson(body, Status.class);

        System.out.println(status.toString());

        String currUserAlias = status.getUser().getAlias();

        //Push list and status to job queue in batches of 25
        FollowerRequest fetchRequest = new FollowerRequest(null, currUserAlias,
                PAGE_SIZE, null);
        FollowerResponse fetchResponse = getFollowDAO().getPagedFollowers(fetchRequest);

        List<User> followers = fetchResponse.getFollowers();
        List<StatusListTable> statusAndFollowers = createStatusAndFollowerList(followers, status);
        pushToJobQueue(statusAndFollowers);

        while (fetchResponse.getHasMorePages()) {
            String lastAlias = followers.get(followers.size() - 1).getAlias();
            fetchRequest = new FollowerRequest(null, currUserAlias,
                    PAGE_SIZE, lastAlias);
            fetchResponse = getFollowDAO().getPagedFollowers(fetchRequest);

            followers = fetchResponse.getFollowers();
            System.out.println("followers size: " + followers.size());
            statusAndFollowers = createStatusAndFollowerList(followers, status);
            pushToJobQueue(statusAndFollowers);
        }
    }

    private List<StatusListTable> createStatusAndFollowerList(List<User> followers, Status status) {
        List<StatusListTable> statusAndFollowers = new ArrayList<>();
        for (User follower : followers) {
            StatusListTable newStatusAndFollower = new StatusListTable();
            User author = status.getUser();

            newStatusAndFollower.setUser_alias(follower.getAlias());
            newStatusAndFollower.setTimestamp(status.getDate());

            newStatusAndFollower.setPost(status.getPost());
            newStatusAndFollower.setMentions(status.getMentions());
            newStatusAndFollower.setUrls(status.getUrls());

            newStatusAndFollower.setUserFirstName(author.getFirstName());
            newStatusAndFollower.setUserLastName(author.getLastName());
            newStatusAndFollower.setUserImageURL(author.getImageUrl());
            newStatusAndFollower.setAuthorAlias(author.getAlias());

            statusAndFollowers.add(newStatusAndFollower);
        }

        return statusAndFollowers;
    }

    private void pushToJobQueue(List<StatusListTable> statusAndFollowers) {
        // Call lambda to batch write to feed
        Gson gson = new Gson();
        String messageBody = gson.toJson(statusAndFollowers);

        String SQS_JOB_URL = "https://sqs.us-west-2.amazonaws.com/375136350162/JobQueue";

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(SQS_JOB_URL)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

        sqs.sendMessage(request);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAOInterface getFollowDAO() {
        return factory.getFollowDAO();
    }

    UserDAOInterface getUserDAO() {return factory.getUserDAO();}
}
