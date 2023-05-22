package com.FunctionApp.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.Cardinality;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

public class EventhubReceiver {
    @FunctionName("EventHubReceiver")
    public void run(
            @EventHubTrigger(name = "message",
                    eventHubName = "your-event-hub-name",
                    connection = "EventHubConnection",
                    consumerGroup = "$Default",
                    cardinality = Cardinality.MANY)
                    String message,
            final ExecutionContext context
    ) {
        context.getLogger().info("Received message: " + message);
        // Process the received message
    }
}

