package com.papaya.cycleactivitylog.service.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class LoggedItemsSearchCriteria {
    private final @NotEmpty String cycleId;

    private final Period period;

    private final List<String> sources;

    private final List<String> states;

    private final List<LoggedItemEventType> eventTypes;
}
