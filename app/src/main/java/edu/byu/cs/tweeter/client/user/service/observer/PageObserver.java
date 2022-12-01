package edu.byu.cs.tweeter.client.user.service.observer;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public interface PageObserver<T> extends ServiceObserver {
    void addElements(List<T> elements, boolean hasMorePages);
}
