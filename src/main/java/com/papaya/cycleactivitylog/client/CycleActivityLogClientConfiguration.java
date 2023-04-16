package com.papaya.cycleactivitylog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.papaya.cycleactivitylog.client.aws.DefaultSqsPublisher;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

@Data
@Builder
public class CycleActivityLogClientConfiguration {
    private static CycleActivityLogClientConfiguration defaultConfiguration;

    private final URI awsEndpointUrl;

    private final String sqsQueueUrl;

    private final Boolean publishEmulationEnabled;

    private final Consumer<ClientOverrideConfiguration.Builder> sqsOverrideConfiguration;

    private final ObjectMapper objectMapper;


    public static CycleActivityLogClientConfiguration defaultConfiguration() {
        if (defaultConfiguration == null) {
            defaultConfiguration = defaultBuilder().build();
        }
        return defaultConfiguration;
    }

    public static CycleActivityLogClientConfigurationBuilder defaultBuilder() {
        var builder = CycleActivityLogClientConfiguration.builder()
                .objectMapper(defaultObjectMapper())
                .sqsOverrideConfiguration(b -> b.retryPolicy(DefaultSqsPublisher.defaultRetryPolicy()))
                .publishEmulationEnabled(EnvironmentVariables.CYCLE_ACTIVITY_LOG_PUBLISH_EMULATION_ENABLED.getBooleanValue());

        Optional.ofNullable(EnvironmentVariables.AWS_ENDPOINT_URL.getValue())
                .ifPresent(awsEndpointUrl -> builder.awsEndpointUrl(URI.create(awsEndpointUrl)));

        return builder;

    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }


}
