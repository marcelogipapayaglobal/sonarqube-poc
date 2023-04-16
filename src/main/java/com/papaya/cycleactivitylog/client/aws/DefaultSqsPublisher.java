package com.papaya.cycleactivitylog.client.aws;

import com.papaya.cycleactivitylog.client.CycleActivityLogClientConfiguration;
import lombok.SneakyThrows;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.RetryPolicyContext;
import software.amazon.awssdk.core.retry.backoff.EqualJitterBackoffStrategy;
import software.amazon.awssdk.core.retry.conditions.RetryCondition;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class DefaultSqsPublisher implements SqsPublisher {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("com.papaya.cycleactivitylog.client");

    private final SqsAsyncClient client;

    public DefaultSqsPublisher(CycleActivityLogClientConfiguration configuration) {
        this(sqsClient(configuration));
    }

    public DefaultSqsPublisher(SqsAsyncClient client) {
        this.client = client;
    }

    public static RetryPolicy defaultRetryPolicy() {
        RetryCondition defaultRetryCondition = RetryCondition.defaultRetryCondition();
        return RetryPolicy.defaultRetryPolicy().toBuilder()
                .retryCondition(new RetryCondition() {

                    @Override
                    public boolean shouldRetry(RetryPolicyContext context) {
                        String text = String.format("[cycle-activity-log] Error sending item [attempt %d]", context.totalRequests());
                        log.error(text, context.exception());
                        return defaultRetryCondition.shouldRetry(context);
                    }

                    @Override
                    public void requestWillNotBeRetried(RetryPolicyContext context) {
                        defaultRetryCondition.requestWillNotBeRetried(context);
                    }

                    @Override
                    public void requestSucceeded(RetryPolicyContext context) {
                        defaultRetryCondition.requestSucceeded(context);
                    }
                })
                .numRetries(5)
                .backoffStrategy(EqualJitterBackoffStrategy.builder()
                        .baseDelay(Duration.ofMillis(500))
                        .maxBackoffTime(Duration.ofSeconds(15))
                        .build())
                .build();
    }

    @Override
    public CompletableFuture<SendMessageResponse> sendMessageAsync(SendMessageRequest request) {
        log.info("[cycle-activity-log] Sending {}", request);

        var response = this.client.sendMessage(request);

        // Some errors are not logged by the retry policy because occur before even trying to send a message.
        // For instance when no credentials are encountered.
        // We capture and log the last error.
        response.handle((result, ex) -> {
            if (ex != null) log.error("[cycle-activity-log] Error sending item", ex);
            return result;
        });

        return response;
    }

    @Override
    @SneakyThrows
    public String getSqsQueueUrl(String sqsQueueName) {
        var request = GetQueueUrlRequest.builder()
                .queueName(sqsQueueName)
                .build();

        return this.client.getQueueUrl(request).get().queueUrl();
    }

    private static SqsAsyncClient sqsClient(CycleActivityLogClientConfiguration configuration) {
        var awsEndpointUrl = configuration.getAwsEndpointUrl();
        var sqsOverrideConfiguration = configuration.getSqsOverrideConfiguration();
        return SqsClients.asyncClient(
                awsEndpointUrl,
                sqsOverrideConfiguration
        );
    }
}
