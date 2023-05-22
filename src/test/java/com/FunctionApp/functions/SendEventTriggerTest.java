package com.FunctionApp.functions;

import com.FunctionApp.functions.SendEventTrigger;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SendEventTriggerTest {

    @Test
    public void run_SendEvent_Success() throws Exception {
        // Mock HttpRequestMessage
        HttpRequestMessage<String> request = mock(HttpRequestMessage.class);
        when(request.getBody()).thenReturn("Test Event Data");

        // Mock ExecutionContext
        ExecutionContext context = mock(ExecutionContext.class);
        when(context.getLogger()).thenReturn(mock(Logger.class));

        // Mock EventHubProducerClient and related objects
        EventHubProducerClient producer = mock(EventHubProducerClient.class);
        EventDataBatch eventDataBatch = mock(EventDataBatch.class);
        when(producer.createBatch()).thenReturn(eventDataBatch);
        when(eventDataBatch.tryAdd(any())).thenReturn(true);
        ArgumentCaptor<EventData> eventDataCaptor = ArgumentCaptor.forClass(EventData.class);

        // Mock EventHubClientBuilder
        EventHubClientBuilder builder = mock(EventHubClientBuilder.class);
        when(builder.fullyQualifiedNamespace(any())).thenReturn(builder);
        when(builder.eventHubName(any())).thenReturn(builder);
        when(builder.buildProducerClient()).thenReturn(producer);

        // Mock EventHubClientBuilder initialization
        try (MockedStatic<EventHubClientBuilder> mockedBuilder = Mockito.mockStatic(EventHubClientBuilder.class)) {
            mockedBuilder.when(EventHubClientBuilder::new).thenReturn(builder);

            // Create an instance of the SendEventTrigger class
            SendEventTrigger function = new SendEventTrigger();

            // Invoke the Azure Function
            String result = function.run(request, context);

            // Verify the result
            assertEquals("ok", result);

            // Verify EventHubProducerClient and EventDataBatch interactions
            verify(eventDataBatch, times(1)).tryAdd(eventDataCaptor.capture());
            verify(producer, times(1)).send(eventDataBatch);
            assertEquals("Test Event Data", new String(eventDataCaptor.getValue().getBody(), StandardCharsets.UTF_8));

            // Verify logging
            verify(context.getLogger(), times(1)).info("Event sent to Event Hub.");
        }
    }
}
