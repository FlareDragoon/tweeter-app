package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowingHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest input, Context context) {
        FollowService service = new FollowService(new DynamoDBFactory());
        return service.isFollowing(input);
    }
}
