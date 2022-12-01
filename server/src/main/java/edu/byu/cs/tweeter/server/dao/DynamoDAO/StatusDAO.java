package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.StatusListTable;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class StatusDAO implements StatusDAOInterface {
    private static final String StoryTableName = "story";
    private static final String FeedTableName = "feed";
    private static final String UserAliasAttr = "user_alias";
    private static final String TimestampAttr = "timestamp";

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

    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        assert request.getStatus() != null;

        Status status = request.getStatus();
        User user = status.getUser();
        String alias = user.getAlias();

        AppendToStory(status, alias);

        return new PostStatusResponse();
    }

    @Override
    public PostStatusResponse postStatus(PostStatusRequest request, List<User> followers) {
        assert request.getStatus() != null;

//        Status status = request.getStatus();
//        User user = status.getUser();
//        String alias = user.getAlias();
//
//        AppendToStory(status, alias);
//
//        for (User follower : followers) {
//            AppendToFeed(status, follower.getAlias());
//        }

        return new PostStatusResponse();
    }

    public void AppendToStory(Status status, String alias) {
        DynamoDbTable<StatusListTable> table = getEnhancedClient()
                .table(StoryTableName, TableSchema.fromBean(StatusListTable.class));

        String timestamp = status.getDate();

        StatusListTable newStatusListTablePost = new StatusListTable();
        newStatusListTablePost.setUser_alias(alias);
        newStatusListTablePost.setTimestamp(timestamp);

        newStatusListTablePost.setPost(status.getPost());
        newStatusListTablePost.setMentions(status.getMentions());
        newStatusListTablePost.setUrls(status.getUrls());

        User user = status.getUser();

        newStatusListTablePost.setUserFirstName(user.getFirstName());
        newStatusListTablePost.setUserLastName(user.getLastName());
        newStatusListTablePost.setUserImageURL(user.getImageUrl());

        table.putItem(newStatusListTablePost);
    }

    public void AppendToFeed(Status status, String alias) {
        System.out.println("Getting feed table");
        DynamoDbTable<StatusListTable> table = getEnhancedClient()
                .table(FeedTableName, TableSchema.fromBean(StatusListTable.class));

        String timestamp = status.getDate();

        StatusListTable newFeedPost = new StatusListTable();
        newFeedPost.setUser_alias(alias);
        newFeedPost.setTimestamp(timestamp);

        newFeedPost.setPost(status.getPost());
        newFeedPost.setMentions(status.getMentions());
        newFeedPost.setUrls(status.getUrls());

        User user = status.getUser();

        newFeedPost.setUserFirstName(user.getFirstName());
        newFeedPost.setUserLastName(user.getLastName());
        newFeedPost.setUserImageURL(user.getImageUrl());

        System.out.println("Putting into feed");
        table.putItem(newFeedPost);

    }

    @Override
    public StoryResponse getPagedStory(StoryRequest storyRequest) {
        DynamoDbTable<StatusListTable> table = getEnhancedClient()
                .table(StoryTableName, TableSchema.fromBean(StatusListTable.class));

        String userAlias = storyRequest.getCurrUserAlias();
        String lastTimeStamp = storyRequest.getLastTimeStamp();

        int pageSize = storyRequest.getLimit();

        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(true).queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        QueryEnhancedRequest request = requestBuilder.build();

        if(isNonEmptyString(lastTimeStamp)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().s(lastTimeStamp).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        List<StatusListTable> statusListTableData = table.query(request)
                .items()
                .stream()
                .limit(pageSize)
                .collect(Collectors.toList());

        List<Status> storyList = new ArrayList<>();

        for (StatusListTable i : statusListTableData) {
            User user = new User(i.getUserFirstName(), i.getUserLastName(),
                    i.getUser_alias(), i.getUserImageURL());
            Status status = new Status(i.getPost(), user, i.getTimestamp(),
                    i.getUrls(), i.getMentions());
            storyList.add(status);
        }

        boolean hasMorePages = false;

        if (storyList.size() == pageSize) {
            hasMorePages = true;
        }

        return new StoryResponse(storyList, hasMorePages);
    }

    @Override
    public FeedResponse getPagedFeed(FeedRequest feedRequest) {
        DynamoDbTable<StatusListTable> table = getEnhancedClient()
                .table(FeedTableName, TableSchema.fromBean(StatusListTable.class));

        String userAlias = feedRequest.getCurrUserAlias();
        String lastTimeStamp = feedRequest.getLastTimeStamp();

        int pageSize = feedRequest.getLimit();

        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(true).queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        QueryEnhancedRequest request = requestBuilder.build();

        if(isNonEmptyString(lastTimeStamp)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().s(lastTimeStamp).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        List<StatusListTable> statusListTableData = table.query(request)
                .items()
                .stream()
                .limit(pageSize)
                .collect(Collectors.toList());

        List<Status> feedList = new ArrayList<>();

        for (StatusListTable i : statusListTableData) {
            User user = new User(i.getUserFirstName(), i.getUserLastName(),
                    i.getAuthorAlias(), i.getUserImageURL());
            Status status = new Status(i.getPost(), user, i.getTimestamp(),
                    i.getUrls(), i.getMentions());
            feedList.add(status);
        }

        boolean hasMorePages = false;

        if (feedList.size() == pageSize) {
            hasMorePages = true;
        }
        System.out.println(feedList.get(feedList.size()-1).datetime);

        return new FeedResponse(feedList, hasMorePages);
    }

    @Override
    public void batchPost(List<StatusListTable> statusListTables) {
        // batch write to feed table
        DynamoDbTable<StatusListTable> table = getEnhancedClient()
                .table(FeedTableName, TableSchema.fromBean(StatusListTable.class));

        WriteBatch.Builder<StatusListTable> batchBuilder = WriteBatch.builder(StatusListTable.class)
                .mappedTableResource(table);
        statusListTables.forEach(batchBuilder::addPutItem);

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .addWriteBatch(batchBuilder.build())
                .build();

        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

}
