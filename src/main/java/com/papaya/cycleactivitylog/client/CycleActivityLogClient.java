package com.papaya.cycleactivitylog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papaya.cycleactivitylog.client.aws.DefaultSqsPublisher;
import com.papaya.cycleactivitylog.client.aws.SqsPublisherEmulator;
import com.papaya.cycleactivitylog.client.aws.SqsPublisher;
import lombok.SneakyThrows;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * This is the access point for logging items to the Cycle Activity Logger.
 * The method {@link #defaultClient()} provides a default client.
 */
public class CycleActivityLogClient {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("com.papaya.cycleactivitylog.client");

    private static CycleActivityLogClient defaultClient;

    private final String queueUrl;

    private final ObjectMapper objectMapper;

    private final SqsPublisher sqsPublisher;

    /**
     * Creates an instance of a client with a provided configuration.
     *
     * @param configuration: configuration of the client
     */
    public CycleActivityLogClient(CycleActivityLogClientConfiguration configuration) {
        this.sqsPublisher = configuration.getPublishEmulationEnabled()
                ? new SqsPublisherEmulator(configuration)
                : new DefaultSqsPublisher(configuration);
        this.queueUrl = configuration.getSqsQueueUrl();
        this.objectMapper = configuration.getObjectMapper();

        log.info("[cycle-activity-log] New CycleActivityLogClient with configuration: {}", configuration);
    }

    public static CycleActivityLogClient defaultClient() {
        if (defaultClient == null) {
            defaultClient = new CycleActivityLogClient(CycleActivityLogClientConfiguration.defaultBuilder()
                    .sqsQueueUrl(EnvironmentVariables.CYCLE_ACTIVITY_LOG_SQS_QUEUE_URL.getValue())
                    .build()
            );
        }
        return defaultClient;
    }

    public LoggedItem info(LoggedItem item) {
        return this.log(item, LoggedItemSeverity.Info);
    }

    public LoggedItem error(LoggedItem item) {
        return this.log(item, LoggedItemSeverity.Error);
    }

    public LoggedItem warning(LoggedItem item) {
        return this.log(item, LoggedItemSeverity.Warning);
    }

    public LoggedItem log(LoggedItem item, LoggedItemSeverity severity) {
        return log(item, severity, LocalDateTime.now(ZoneOffset.UTC));
    }

    protected LoggedItem log(LoggedItem item, LoggedItemSeverity severity, LocalDateTime when) {
        var clone = item.clone();
        clone.setSeverity(severity);
        clone.setWhen(when);
        if (clone.getOccurrence() == null) {
            clone.setOccurrence(when);
        }
        this.sqsPublisher.sendMessageAsync(sendMessageRequest(clone));
        return clone;
    }


    private SendMessageRequest sendMessageRequest(LoggedItem item) {
        return SendMessageRequest.builder()
                .queueUrl(this.queueUrl)
                .messageBody(serialize(item))
                .messageGroupId(item.getCycleId())
                .build();
    }

    @SneakyThrows
    private String serialize(LoggedItem item) {
        return this.objectMapper.writeValueAsString(item);
    }


}
