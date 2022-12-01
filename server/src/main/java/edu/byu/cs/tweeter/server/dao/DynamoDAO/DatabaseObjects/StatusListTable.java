package edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class StatusListTable {
    private String user_alias;
    private String timestamp;

    private String post;
    private List<String> urls;
    private List<String> mentions;

    private String userFirstName;
    private String userLastName;
    private String userImageURL;

    private String authorAlias;

    @DynamoDbPartitionKey
    public String getUser_alias() {
        return user_alias;
    }

    public void setUser_alias(String userAlias) {
        this.user_alias = userAlias;
    }

    @DynamoDbSortKey
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getAuthorAlias() {
        return authorAlias;
    }

    public void setAuthorAlias(String authorAlias) {
        this.authorAlias = authorAlias;
    }
}
