package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class FeedWriterHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        StatusService service = new StatusService(new DynamoDBFactory());
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println(msg.getBody());
            service.writeToFeed(msg.getBody());
        }
        return null;
    }
}
