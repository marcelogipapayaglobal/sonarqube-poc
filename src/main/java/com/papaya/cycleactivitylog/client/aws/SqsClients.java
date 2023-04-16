package com.papaya.cycleactivitylog.client.aws;

import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

public class SqsClients {

    public static SqsClient syncClient(
            URI awsEndpoint,
            Consumer<ClientOverrideConfiguration.Builder> overrideConfiguration) {

        SqsClientBuilder builder = SqsClient.builder();

        Optional.ofNullable(awsEndpoint).ifPresent(builder::endpointOverride);
        Optional.ofNullable(overrideConfiguration).ifPresent(builder::overrideConfiguration);

        return builder.build();
    }

    public static SqsAsyncClient asyncClient(
            URI awsEndpointUrl,
            Consumer<ClientOverrideConfiguration.Builder> overrideConfiguration) {

        SqsAsyncClientBuilder builder = SqsAsyncClient.builder();

        Optional.ofNullable(awsEndpointUrl).ifPresent(builder::endpointOverride);
        Optional.ofNullable(overrideConfiguration).ifPresent(builder::overrideConfiguration);

        return builder.build();
    }
}
