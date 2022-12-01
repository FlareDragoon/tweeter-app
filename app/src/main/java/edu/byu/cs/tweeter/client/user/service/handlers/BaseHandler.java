package edu.byu.cs.tweeter.client.user.service.handlers;
import android.os.Handler;

import edu.byu.cs.tweeter.client.user.service.observer.ServiceObserver;

public class BaseHandler<T extends ServiceObserver> extends Handler{
    T observer;
    BaseHandler(T observer) {
        this.observer = observer;
    }


}
