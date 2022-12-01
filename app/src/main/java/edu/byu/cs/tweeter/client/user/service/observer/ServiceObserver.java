package edu.byu.cs.tweeter.client.user.service.observer;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception exception);
}


