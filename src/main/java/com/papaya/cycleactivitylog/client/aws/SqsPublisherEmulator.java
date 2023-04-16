package com.papaya.cycleactivitylog.client.aws;

import com.papaya.cycleactivitylog.client.CycleActivityLogClientConfiguration;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

public class SqsPublisherEmulator implements SqsPublisher {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("com.papaya.cycleactivitylog.client");

    private final String sqsQueueName;

    public SqsPublisherEmulator(CycleActivityLogClientConfiguration configuration) {
        this.sqsQueueName = configuration.getSqsQueueUrl();
    }

    @Override
    public CompletableFuture<SendMessageResponse> sendMessageAsync(SendMessageRequest request) {
        log.info("[cycle-activity-log] Sending {}", request);
        return new CompletableFuture<>();
    }

    @Override
    public String getSqsQueueUrl(String sqsQueueName) {
        return "emulated:" + this.sqsQueueName;
    }


}
