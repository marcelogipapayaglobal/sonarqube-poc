package com.papaya.cycleactivitylog.client.aws;

import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

public interface SqsPublisher {
    CompletableFuture<SendMessageResponse> sendMessageAsync(SendMessageRequest request);

    String getSqsQueueUrl(String sqsQueueName);
}
