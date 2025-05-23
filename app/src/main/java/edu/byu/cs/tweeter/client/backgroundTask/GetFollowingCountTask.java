package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    private static final String URL_PATH = "/getfollowingcount";


    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected void runTask() throws IOException {
        try {
            GetCountRequest request = new GetCountRequest(targetUser);
            GetCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);

            if (response.isSuccess()) {
                this.count = response.getCount();
                sendSuccessMessage();
            }else {
                sendFailedMessage("Could not retrieve count data.");
            }

        } catch(Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    @Override
    protected int runCountTask() {
        return 20;
    }
}
