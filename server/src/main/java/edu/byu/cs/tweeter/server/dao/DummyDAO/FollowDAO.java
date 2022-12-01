package edu.byu.cs.tweeter.server.dao.DummyDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO {

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param user the User whose count of how many following is desired.
     * @return said count.
     */
//    @Override
//    public Integer getFollowingCount(User user) {
//        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert user != null;
//        return getDummyUsers().size() - 1;
//    }

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
//    @Override
    public FollowingResponse getFollowees(FollowingRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getCurrUserAlias() != null;

        List<User> allFollowees = getDummyUsers();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getStartingIndex(request.getLastListedAlias(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
//    @Override
//    public Integer getFollowerCount(User follower) {
//        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert follower != null;
//        return getDummyUsers().size() - 1;
//    }

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
//    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getCurrUserAlias() != null;

        List<User> allFollowers = getDummyUsers();
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followeesIndex = getStartingIndex(request.getLastListedAlias(), allFollowers);

                for(int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

//    @Override
    public List<User> getAllFollowers(User followee) {
        return null;
    }

    /**
     * Determines the index for the first followee in the specified 'userList' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param userList the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
//    @Override
    public int getStartingIndex(String lastAlias, List<User> userList) {

        int currIndex = 0;

        if(lastAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < userList.size(); i++) {
                if(lastAlias.equals(userList.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    currIndex = i + 1;
                    break;
                }
            }
        }

        return currIndex;
    }

//    @Override
    public FollowResponse followUser(FollowRequest request) {
        assert request.getFollowee() != null;

        //TODO: Implement in M4
        return new FollowResponse();
    }

//    @Override
    public UnfollowResponse unfollowUser(UnfollowRequest request) {
        assert request.getFollowee() != null;

        //TODO: Implement in M4
        return new UnfollowResponse();
    }

//    @Override
    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        assert request.getFollowee() != null;

        //TODO: Implement in M4
        boolean isFollower = new Random().nextInt() > 0;

        return new IsFollowerResponse(isFollower);
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyUsers() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
