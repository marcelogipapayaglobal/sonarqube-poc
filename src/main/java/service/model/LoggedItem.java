package service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggedItem {
    private @NonNull String cycleId;
    private String hash;
    private @NonNull LocalDateTime when;
    private @NonNull LocalDateTime occurrence;
    private @NonNull String source;
    private @NonNull String event;
    private String state;
    private @NonNull LoggedItemSeverity severity;
    private @NonNull LoggedItemEventType eventType;
    private String user;
    private String summary;


}

