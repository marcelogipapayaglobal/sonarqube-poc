package com.papaya.cycleactivitylog.client;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class LoggedItem implements Cloneable {
    private final @NonNull String cycleId;
    private final @NonNull String source;
    private final @NonNull String event;
    private final String state;
    private final @NonNull LoggedItemEventType eventType;
    private final String user;
    private final String summary;
    private LocalDateTime occurrence;
    private @NonNull
    @Setter(AccessLevel.PROTECTED) LocalDateTime when;
    private @NonNull
    @Setter(AccessLevel.PROTECTED) LoggedItemSeverity severity;

    @Builder
    public LoggedItem(
            @NonNull String cycleId,
            LocalDateTime occurrence,
            @NonNull String source,
            @NonNull String event,
            String state,
            @NonNull LoggedItemEventType eventType,
            String user,
            String summary) {
        this.cycleId = cycleId;
        this.occurrence = occurrence;
        this.source = source;
        this.event = event;
        this.state = state;
        this.eventType = eventType;
        this.user = user;
        this.summary = summary;
    }

    @Override
    public LoggedItem clone() {
        try {
            return (LoggedItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public LoggedItem logInfo() {
        return this.log(LoggedItemSeverity.Info, CycleActivityLogClient.defaultClient());
    }

    public LoggedItem logWarning() {
        return this.log(LoggedItemSeverity.Warning, CycleActivityLogClient.defaultClient());
    }

    public LoggedItem logError() {
        return this.log(LoggedItemSeverity.Error, CycleActivityLogClient.defaultClient());
    }

    public LoggedItem log(LoggedItemSeverity severity) {
        return this.log(severity, CycleActivityLogClient.defaultClient());
    }

    public LoggedItem log(LoggedItemSeverity severity, CycleActivityLogClient client) {
        return client.log(this, severity);
    }
}

