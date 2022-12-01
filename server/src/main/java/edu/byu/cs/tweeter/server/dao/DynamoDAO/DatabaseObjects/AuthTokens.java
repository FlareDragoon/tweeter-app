package edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class AuthTokens {
    String token;
    String dateTime;

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
