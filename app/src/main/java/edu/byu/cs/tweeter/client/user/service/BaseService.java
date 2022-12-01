package edu.byu.cs.tweeter.client.user.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

import edu.byu.cs.tweeter.client.user.service.observer.ServiceObserver;

public abstract class BaseService {
    public final <T extends Runnable> void executeTask(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public abstract class BaseHandler extends Handler {
        protected ServiceObserver observer;
        public BaseHandler (ServiceObserver observer) {
            this.observer = observer;
        }
    }




}
