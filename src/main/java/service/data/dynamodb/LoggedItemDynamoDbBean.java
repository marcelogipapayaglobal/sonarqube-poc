package service.data.dynamodb;

import com.papaya.cycleactivitylog.service.model.LoggedItem;
import com.papaya.cycleactivitylog.service.model.LoggedItemEventType;
import com.papaya.cycleactivitylog.service.model.LoggedItemSeverity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;


@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedItemDynamoDbBean {

    private String cycleId;

    private String hash;

    private String when;

    private String occurrence;

    private String source;

    private String event;

    private String state;

    private String severity;

    private String eventType;

    private String user;

    private String summary;

    public static LoggedItemDynamoDbBean fromLoggedItem(LoggedItem loggedItem) {
        return new LoggedItemDynamoDbBean(
                loggedItem.getCycleId(),
                loggedItem.getHash(),
                loggedItem.getWhen().toString(),
                loggedItem.getOccurrence().toString(),
                loggedItem.getSource(),
                loggedItem.getEvent(),
                loggedItem.getState(),
                loggedItem.getSeverity().name(),
                loggedItem.getEventType().name(),
                loggedItem.getUser(),
                loggedItem.getSummary()
        );
    }

    @DynamoDbPartitionKey
    public String getCycleId() {
        return cycleId;
    }

    @DynamoDbSortKey
    public String getHash() {
        return hash;
    }

    @DynamoDbAttribute(value = "when")
    public String getWhen() {
        return when;
    }

    @DynamoDbAttribute(value = "source")
    public String getSource() {
        return source;
    }

    @DynamoDbAttribute(value = "event")
    public String getEvent() {
        return event;
    }

    @DynamoDbAttribute(value = "state")
    public String getState() {
        return state;
    }

    @DynamoDbAttribute(value = "severity")
    public String getSeverity() {
        return severity;
    }

    @DynamoDbAttribute(value = "eventType")
    public String getEventType() {
        return eventType;
    }

    @DynamoDbAttribute(value = "user")
    public String getUser() {
        return user;
    }

    @DynamoDbAttribute(value = "summary")
    public String getSummary() {
        return summary;
    }

    public LoggedItem toLoggedItem() {
        return new LoggedItem(
                this.cycleId,
                this.hash,
                LocalDateTime.parse(this.when),
                LocalDateTime.parse(this.occurrence),
                this.source,
                this.event,
                this.state,
                LoggedItemSeverity.valueOf(this.severity),
                LoggedItemEventType.valueOf(this.eventType),
                this.user,
                this.summary
        );
    }
}
