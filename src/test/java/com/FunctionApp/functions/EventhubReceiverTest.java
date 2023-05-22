package com.FunctionApp.functions;

import com.microsoft.azure.functions.ExecutionContext;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;
import static org.mockito.Mockito.*;

public class EventhubReceiverTest {

    @Test
    public void run_EventHubTrigger_Success() {
        // Mock the input message
        String message = "Test Event Data";

        // Mock ExecutionContext
        ExecutionContext context = mock(ExecutionContext.class);
        when(context.getLogger()).thenReturn(mock(Logger.class));
        EventhubReceiver function = new EventhubReceiver();

        // Invoke the Azure Function
        function.run(message, context);

        // Verify logging
        verify(context.getLogger(), times(1)).info("Received message: " + message);
    }
}
