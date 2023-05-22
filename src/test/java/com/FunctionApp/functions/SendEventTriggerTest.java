package com.FunctionApp.functions;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SendEventTriggerTest {
    @Mock
    private HttpRequestMessage<String> request;
    @Mock
    private ExecutionContext context;

    private SendEventTrigger sendEventTrigger = new SendEventTrigger();

    @Test
    void run_whenEventHubUriIsNull_shouldThrowException() {
        // Arrange
        sendEventTrigger.eventHubUri = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> sendEventTrigger.run(request, context));
    }
}

