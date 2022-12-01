package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.user.service.StatusService;

public class MainPresenterUnitTest {
    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
    }

    @Test
    public void testPost_Success() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostObserver observer = invocation.getArgument(2,
                        StatusService.PostObserver.class);
                observer.performAction("post");
                verifyFunctionality(this, "Successfully Posted!");

                return null;
            }
        };


    }

    @Test
    public void testPost_Failure() {

        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostObserver observer = invocation.getArgument(2,
                        StatusService.PostObserver.class);
                observer.handleFailure("post failed");

                Mockito.verify(mockView).displayMessage("post failed");
                verifyFunctionality(this, "post failed");

                return null;
            }
        };

    }

    @Test
    public void testPost_Exception() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostObserver observer = invocation.getArgument(2,
                        StatusService.PostObserver.class);
                observer.handleException(new Exception("exception thrown"));
                verifyFunctionality(this, "Failed to post status because of exception: exception thrown");
//                Assertions.assertEquals(mockStatusService.);
                return null;
            }
        };
    }

    private void verifyFunctionality(Answer<Void> answer, String message) {
        mainPresenterSpy.initiatePost("post");

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(),
                Mockito.any(), Mockito.any());

        Mockito.verify(mockView).displayMessage(message);
    }
}
