package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.util.FakeData;
import okhttp3.Response;

public abstract class BackgroundTask implements Runnable {
    protected static final String LOG_TAG = "BackgroundTask";

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    /**
     * Message handler that will receive task results.
     */
    private final Handler messageHandler;

    protected ServerFacade serverFacade;

    protected BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            runTask();
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected abstract <U extends Response> void runTask() throws IOException;

    protected FakeData getFakeData() {
        return new FakeData();
    }

    /**
     * Called by a Task's runTask method when it is successful.
     *
     * This method is public to make it accessible to test cases
     */
    public void sendSuccessMessage() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        loadSuccessBundle(msgBundle);
        sendMessage(msgBundle);
    }

    /**
     * Called by a Task's runTask method when it is not successful.
     *
     * This method is public to make it accessible to test cases
     */
    public void sendFailedMessage(String errorMessage) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, errorMessage);
        sendMessage(msgBundle);
    }

    /**
     * Called by a Task's runTask method when an exception occurs.
     *
     * This method is public to make it accessible to test cases
     */
    public void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);
        sendMessage(msgBundle);
    }

    /**
     * Add additional information during a successful task to a Bundle
     * @param msgBundle The bundle send to the handler with the results of the task
     */
    protected void loadSuccessBundle(Bundle msgBundle) {
        // By default, do nothing
    }

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);
        messageHandler.sendMessage(msg);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return serverFacade;
    }
}
