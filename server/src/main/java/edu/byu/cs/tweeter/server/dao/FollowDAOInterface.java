package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.Follows;

public interface FollowDAOInterface {
    List<User> getAllFollowers(User followee);

    FollowResponse followUser(FollowRequest request);

    UnfollowResponse unfollowUser(UnfollowRequest request);

    IsFollowerResponse isFollowing(IsFollowerRequest request);

    FollowingResponse getPagedFollowees(FollowingRequest followingRequest);

    FollowerResponse getPagedFollowers(FollowerRequest followerRequest);
}
