package com.papaya.cycleactivitylog.service.configuration;

import com.papaya.cycleactivitylog.service.EnvironmentVariables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.util.Optional;

@Configuration
public class SqsConfiguration {

    @Bean
    public SqsClient sqsClient() {
        var builder = SqsClient.builder();

        Optional.ofNullable(EnvironmentVariables.AWS_ENDPOINT_URL.getValue())
                .ifPresent(awsEndpointUrl -> builder.endpointOverride(URI.create(awsEndpointUrl)));

        return builder.build();

    }

}
