package com.FunctionApp.functions;

import com.azure.messaging.eventhubs.*;
import com.microsoft.azure.eventhubs.EventHubException;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.beans.factory.annotation.Value;

public class SendEventTrigger {
    @Value("${eventhub.uri}")
    private String eventHubUri;
    @Value("${eventhub.name}")
    private String eventHubName;

    @FunctionName("sendEvent")
    public String run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<String> request,
            final ExecutionContext context
    ) throws EventHubException {
        EventHubProducerClient producer = new EventHubClientBuilder()
                .fullyQualifiedNamespace(eventHubUri)
                .eventHubName(eventHubName)
                .buildProducerClient();

        String eventData = request.getBody();
        EventData event = new EventData(eventData.getBytes());
        EventDataBatch eventDataBatch = producer.createBatch();
        eventDataBatch.tryAdd(event);
        producer.send(eventDataBatch);
        context.getLogger().info("Event sent to Event Hub.");
        return "ok";
    }
}