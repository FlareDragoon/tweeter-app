package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.Users;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO implements FollowDAOInterface {
    public static final String IndexName = "follows_index";
    private static final String TableName = "follows";
    private static final String FollowerAliasAttr = "follower_handle";
    private static final String FolloweeAliasAttr = "followee_handle";

    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient enhancedClient;

    public DynamoDbEnhancedClient getEnhancedClient() {
        if (dynamoDbClient == null) {
            dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_WEST_2)
                    .build();
        }
        System.out.println("Db client made");

        if (enhancedClient == null) {
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }
        System.out.println("Enhanced CLient made");

        return enhancedClient;
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    @Override
    public FollowResponse followUser(FollowRequest request) {
        assert request.getFollowee() != null;
        assert request.getFollower() != null;

        DynamoDbTable<Follows> table = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class));

        User follower = request.getFollower();
        User followee = request.getFollowee();

        String followerAlias = follower.getAlias();
        String followerFirstName = follower.getName();
        String followerLastName = follower.getLastName();
        String followerImageURL = follower.getImageUrl();

        String followeeAlias = followee.getAlias();
        String followeeFirstName = followee.getName();
        String followeeLastName = followee.getLastName();
        String followeeImageURL = followee.getImageUrl();

        Follows newFollowsData = new Follows();

        newFollowsData.setFollower_handle(followerAlias);
        newFollowsData.setFollowerFirstName(followerFirstName);
        newFollowsData.setFollowerLastName(followerLastName);
        newFollowsData.setFollowerImageURL(followerImageURL);

        newFollowsData.setFollowee_handle(followeeAlias);
        newFollowsData.setFolloweeFirstName(followeeFirstName);
        newFollowsData.setFolloweeLastName(followeeLastName);
        newFollowsData.setFolloweeImageURL(followeeImageURL);

        table.putItem(newFollowsData);

        return new FollowResponse();
    }

    @Override
    public UnfollowResponse unfollowUser(UnfollowRequest request) {
        assert request.getFollowee() != null;
        assert request.getFollower() != null;

        DynamoDbTable<Follows> table = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class));
        String followerAlias = request.getFollower().getAlias();
        String followeeAlias = request.getFollowee().getAlias();

        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();

        table.deleteItem(key);

        return new UnfollowResponse();
    }

    @Override
    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        assert request.getFollowee() != null;
        assert request.getFollower() != null;

        DynamoDbTable<Follows> table = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class));

        String followerAlias = request.getFollower().getAlias();
        String followeeAlias = request.getFollowee().getAlias();

        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();

        Follows followsData = table.getItem(key);

        System.out.println("Item received");

        boolean isFollower = true;

        try {
            if (followsData.getFollowee_handle().equals(null)) {
                isFollower = false;
            }
        } catch (NullPointerException ex) {
            isFollower = false;
        }

        System.out.println("isFollower: " + isFollower);

        return new IsFollowerResponse(isFollower);
    }

    public List<Follows> getAllFollowees(User follower) {
        DynamoDbTable<Follows> table = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class));
        String followerAlias = follower.getAlias();

        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(true).queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        return table.query(request)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFollowers(User followee) {
        DynamoDbIndex<Follows> index = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class)).index(IndexName);
        String followeeAlias = followee.getAlias();

        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(false).queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        List<Follows> allFollowers = new ArrayList<>();

        SdkIterable<Page<Follows>> results2 = index.query(request);
        PageIterable<Follows> pages = PageIterable.create(results2);
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> visitsPage.items().forEach(v -> allFollowers.add(v)));

        List<User> allUsers = new ArrayList<>();
        for (Follows i : allFollowers) {
            User user = new User(i.getFollowerFirstName(), i.getFollowerLastName(),
                    i.getFollower_handle(), i.getFollowerImageURL());
            allUsers.add(user);
        }

        return allUsers;
    }



    @Override
    public FollowingResponse getPagedFollowees(FollowingRequest followingRequest) {
        DynamoDbTable<Follows> table = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class));

        String followerAlias = followingRequest.getCurrUserAlias();
        String lastFolloweeAlias = followingRequest.getLastListedAlias();
        int pageSize = followingRequest.getLimit();

        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(true).queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        if(isNonEmptyString(lastFolloweeAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAliasAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeAliasAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

            System.out.println("Start key: " + startKey);

            requestBuilder.exclusiveStartKey(startKey);
        }

        List<Follows> followsData = table.query(request)
                .items()
                .stream()
                .limit(pageSize)
                .collect(Collectors.toList());

        List<User> followeeList = new ArrayList<>();

        for (Follows i : followsData) {
            User user = new User(i.getFolloweeFirstName(), i.getFolloweeLastName(),
                    i.getFollowee_handle(), i.getFolloweeImageURL());
            followeeList.add(user);
        }

        boolean hasMorePages = false;

        if (followeeList.size() == pageSize) {
            hasMorePages = true;
        }

        return new FollowingResponse(followeeList, hasMorePages);
    }

    @Override
    public FollowerResponse getPagedFollowers(FollowerRequest followerRequest) {
        DynamoDbIndex<Follows> index = getEnhancedClient()
                .table(TableName, TableSchema.fromBean(Follows.class)).index(IndexName);

        String followeeAlias = followerRequest.getCurrUserAlias();
        int pageSize = followerRequest.getLimit();
        String lastFollowerAlias = followerRequest.getLastListedAlias();

        System.out.println("followeeAlias: " + followeeAlias);

        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        System.out.println("Key made");

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(false).limit(pageSize)
                .queryConditional(QueryConditional.keyEqualTo(key));

        System.out.println("requestBuilder made");

        if(isNonEmptyString(lastFollowerAlias)) {
            System.out.println(lastFollowerAlias);

            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAliasAttr, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(FollowerAliasAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            System.out.println("Start key: " + startKey.toString());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<Follows> allFollowers = new ArrayList<>();

        SdkIterable<Page<Follows>> results2 = index.query(request);
        PageIterable<Follows> pages = PageIterable.create(results2);
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> visitsPage.items().forEach(v -> allFollowers.add(v)));


        List<User> followerList = new ArrayList<>();

        for (Follows i : allFollowers) {
            User user = new User(i.getFollowerFirstName(), i.getFollowerLastName(),
                    i.getFollower_handle(), i.getFollowerImageURL());
            followerList.add(user);
        }

        boolean hasMorePages = false;

        if (followerList.size() == pageSize) {
            hasMorePages = true;
        }

        System.out.println("hasMorePages: " + hasMorePages);

        return new FollowerResponse(followerList, hasMorePages);
    }
}
