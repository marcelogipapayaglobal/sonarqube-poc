package service.data.dynamodb;

import com.papaya.cycleactivitylog.service.EnvironmentVariables;
import com.papaya.cycleactivitylog.service.data.Batch;
import com.papaya.cycleactivitylog.service.data.LoggedItemsReadRepository;
import com.papaya.cycleactivitylog.service.data.LoggedItemsWriteRepository;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;

import java.util.List;

@Repository
@Primary
public class DynamoDbLoggedItemsRepository implements LoggedItemsReadRepository, LoggedItemsWriteRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private final DynamoDbTable<LoggedItemDynamoDbBean> loggedItemsTable;

    @Autowired
    public DynamoDbLoggedItemsRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.loggedItemsTable = dynamoDbEnhancedClient.table(
                EnvironmentVariables.CYCLE_ACTIVITY_LOG_DYNAMODB_LOGGED_ITEMS_TABLE_NAME.getValue(),
                TableSchema.fromBean(LoggedItemDynamoDbBean.class)
        );
    }

    @Override
    public List<LoggedItem> findByCycleId(@NotNull String cycleId) {
//        return List.of(
//                new LoggedItem(
//                        cycleId,
//                        "1",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "GOV",
//                        "GET_TIMELINE",
//                        "Payroll",
//                        LoggedItemSeverity.Info,
//                        LoggedItemEventType.Business,
//                        null,
//                        "a summary"
//                ),
//                new LoggedItem(
//                        cycleId,
//                        "1",
//                        LocalDateTime.now(),
//                        LocalDateTime.now(),
//                        "GOV",
//                        "GET_TIMELINE",
//                        "Payroll",
//                        LoggedItemSeverity.Error,
//                        LoggedItemEventType.Technical,
//                        null,
//                        "a summary"
//                )
//        );
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(cycleId)
                .build());

        return this.loggedItemsTable.query(queryConditional).items().stream()
                .map(LoggedItemDynamoDbBean::toLoggedItem)
                .toList();
    }

    @Override
    public void save(@NotNull Batch<LoggedItem> batch) {
        if (batch.getItems().isEmpty()) {
            return;
        }

        var writeBatchbuilder = WriteBatch.builder(LoggedItemDynamoDbBean.class).mappedTableResource(this.loggedItemsTable);
        for (LoggedItem loggedItem : batch.getItems()) {
            writeBatchbuilder.addPutItem(
                    itemBuilder -> itemBuilder.item(LoggedItemDynamoDbBean.fromLoggedItem(loggedItem))
            );
        }
        var writeBatch = writeBatchbuilder.build();
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBatch)
                .build();

        this.dynamoDbEnhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
    }
}
