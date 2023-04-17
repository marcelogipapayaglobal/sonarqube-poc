package com.papaya.cycleactivitylog.service.configuration;

import com.papaya.cycleactivitylog.service.EnvironmentVariables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.util.Optional;

@Configuration
public class DynamoDbConfiguration {

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        var dynamoDbClientBuilder = DynamoDbClient.builder();

        Optional.ofNullable(EnvironmentVariables.AWS_ENDPOINT_URL.getValue())
                .ifPresent(awsEndpointUrl -> dynamoDbClientBuilder.endpointOverride(URI.create(awsEndpointUrl)));

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClientBuilder.build())
                .build();
    }

}
