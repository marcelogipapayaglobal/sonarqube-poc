package com.papaya.cycleactivitylog.service.data.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papaya.cycleactivitylog.service.EnvironmentVariables;
import com.papaya.cycleactivitylog.service.data.Batch;
import com.papaya.cycleactivitylog.service.data.LoggedItemsChannel;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Component
@Slf4j
public class SqsLoggedItemsChannel implements LoggedItemsChannel {
    private final SqsClient sqsClient;

    private final String sqsQueueUrl;

    private final ObjectMapper objectMapper;

    @Autowired
    public SqsLoggedItemsChannel(SqsClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.sqsQueueUrl = EnvironmentVariables.CYCLE_ACTIVITY_LOG_SQS_QUEUE_URL.getValue();
        this.objectMapper = objectMapper;
    }


    @Override
    public Batch<LoggedItem> nextBatch() {
        return new SqsLoggedItemsBatch(this, this.objectMapper);
    }

    protected List<Message> nextMessages() {
        log.info("Receiving messages from queue {}", this.sqsQueueUrl);

        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(this.sqsQueueUrl)
                .maxNumberOfMessages(EnvironmentVariables.CYCLE_ACTIVITY_LOG_SQS_BATCH_SIZE.getIntValue())
                .waitTimeSeconds(EnvironmentVariables.CYCLE_ACTIVITY_LOG_SQS_WAIT_TIME_SECONDS.getIntValue())
                .build();

        return this.sqsClient.receiveMessage(receiveMessageRequest).messages();
    }

    protected void deleteMessages(List<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }

        log.info("Deleting messages from queue {}", this.sqsQueueUrl);

        var deleteEntries = messages.stream()
                .map(message -> DeleteMessageBatchRequestEntry.builder()
                        .id(message.messageId())
                        .receiptHandle(message.receiptHandle())
                        .build())
                .toList();

        DeleteMessageBatchRequest deleteMessageRequest = DeleteMessageBatchRequest.builder()
                .queueUrl(this.sqsQueueUrl)
                .entries(deleteEntries)
                .build();

        sqsClient.deleteMessageBatch(deleteMessageRequest);
    }
}
