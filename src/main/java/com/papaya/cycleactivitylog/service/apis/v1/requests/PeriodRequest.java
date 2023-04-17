package com.papaya.cycleactivitylog.service.apis.v1.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PeriodRequest {
    private final LocalDateTime from;

    private final LocalDateTime to;


}
