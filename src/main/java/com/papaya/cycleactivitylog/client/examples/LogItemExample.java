package com.papaya.cycleactivitylog.client.examples;

import com.papaya.cycleactivitylog.client.CycleActivityLogClient;
import com.papaya.cycleactivitylog.client.CycleActivityLogClientConfiguration;
import com.papaya.cycleactivitylog.client.LoggedItem;
import com.papaya.cycleactivitylog.client.LoggedItemEventType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * In this example we log items.
 * <p>
 * Requirements:
 * <ul>
 * <li> A running LocalStack instance
 * <li> A FIFO queue for the Cycle Activity Log Service.
 * </ul><p>
 * If you don't have a queue, you can create one with:
 * <p>
 * awslocal sqs create-queue --queue-name "cycle-activity-log.fifo" --attributes "FifoQueue=true,ContentBasedDeduplication=true"
 * <p>
 * In the example we are using a customized configuration.
 * <p>
 * For LocalStack use the following environment variables:
 * AWS_ACCESS_KEY_ID=test;
 * AWS_REGION=us-east-1;
 * AWS_SECRET_ACCESS_KEY=test;
 * CYCLE_ACTIVITY_LOG_SQS_ENDPOINT_URI=<a href="http://localhost:4566"/>
 * <p>
 * If you want to use a profile for AWS credentials, set the profile name in the AWS_PROFILE environment variable
 */
@Slf4j
public class LogItemExample {

    @SneakyThrows
    public static void main(String[] args) {
        try {

            CycleActivityLogClient client = CycleActivityLogClient.defaultClient();

            log.info("We can now log the item.");
            log.info("Logging is non-blocking");
            var loggedItem = LoggedItem.builder()
                    .cycleId("2")
                    .source("AUDIT")
                    .event("GET_TIMELINE")
                    .state("Cycle")
                    .eventType(LoggedItemEventType.BUSINESS)
                    .user("a user")
                    .summary("a summary")
                    .build();

            client.info(loggedItem);
            System.out.println("The client is probably still processing our request, but we are not blocked.");

            System.out.println("The LoggItem class provides facility methods for logging,. Very helpful if you are using the default client.");
            loggedItem.logWarning();
            System.out.println("The was logged with the default client.");

            System.out.println("You can set the occurrence datetime if necessary.");
            loggedItem = LoggedItem.builder()
                    .cycleId("3")
                    .occurrence(LocalDateTime.now(ZoneOffset.UTC))
                    .source("AUDIT")
                    .event("GET_TIMELINE")
                    .eventType(LoggedItemEventType.BUSINESS)
                    .user("a user")
                    .summary("a summary")
                    .build();
            loggedItem.logWarning();

            System.out.println("You can create your own client with your own configuration");
            try {
                client = new CycleActivityLogClient(CycleActivityLogClientConfiguration.builder()
                        .awsEndpointUrl(URI.create("http://localhost:1234"))
                        .sqsQueueUrl("my queue name")
                        .publishEmulationEnabled(false)
                        .build()
                );
            } catch (Exception e) {
                System.out.println("Of course we have an error, the provided endpoint URI is not real!");
            }


            System.out.println("Let's sleep the thread to allow the async process to finish");
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            System.out.println("Did you define your credentials for AWS?");
            e.printStackTrace(System.out);
        }

    }


}
