package edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Users {
    private String user_alias;

    private String firstName;
    private String lastName;
    private String imageURL;
    private String password;

    private int followingCount;
    private int followerCount;

    @DynamoDbPartitionKey
    public String getUser_alias() {
        return user_alias;
    }
    public void setUser_alias(String user_alias) {
        this.user_alias = user_alias;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getFollowingCount() { return followingCount; }
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }
    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
