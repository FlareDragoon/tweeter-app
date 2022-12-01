package edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects;

import edu.byu.cs.tweeter.server.dao.DynamoDAO.FollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Follows {
    private String follower_handle;
    private String followerFirstName;
    private String followee_handle;
    private String followeeFirstName;

    private String followerLastName;
    private String followerImageURL;

    private String followeeLastName;
    private String followeeImageURL;


    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowDAO.IndexName)
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String followerAlias) {
        this.follower_handle = followerAlias;
    }

    public String getFollowerFirstName() {
        return followerFirstName;
    }

    public void setFollowerFirstName(String followerName) {
        this.followerFirstName = followerName;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FollowDAO.IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followeeAlias) {
        this.followee_handle = followeeAlias;
    }

    public String getFolloweeFirstName() {
        return followeeFirstName;
    }

    public void setFolloweeFirstName(String followeeFirstName) {
        this.followeeFirstName = followeeFirstName;
    }


    public String getFollowerLastName() {
        return followerLastName;
    }

    public void setFollowerLastName(String followerLastName) {
        this.followerLastName = followerLastName;
    }

    public String getFollowerImageURL() {
        return followerImageURL;
    }

    public void setFollowerImageURL(String followerImageURL) {
        this.followerImageURL = followerImageURL;
    }

    public String getFolloweeLastName() {
        return followeeLastName;
    }

    public void setFolloweeLastName(String followeeLastName) {
        this.followeeLastName = followeeLastName;
    }

    public String getFolloweeImageURL() {
        return followeeImageURL;
    }

    public void setFolloweeImageURL(String followeeImageURL) {
        this.followeeImageURL = followeeImageURL;
    }
}
