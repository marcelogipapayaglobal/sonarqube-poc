package com.papaya.cycleactivitylog.service.apis.v1.converters;

import com.papaya.cycleactivitylog.service.apis.v1.responses.LoggedItemEventType;
import com.papaya.cycleactivitylog.service.apis.v1.responses.LoggedItemResponse;
import com.papaya.cycleactivitylog.service.apis.v1.responses.LoggedItemSeverity;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import org.springframework.core.convert.converter.Converter;


public class LoggedItemToLoggedItemResponseConverter implements Converter<LoggedItem, LoggedItemResponse> {

    @Override
    public LoggedItemResponse convert(LoggedItem from) {
        return convertTo(from);
    }

    public static LoggedItemResponse convertTo(LoggedItem from) {
        return new LoggedItemResponse(
                from.getCycleId(),
                from.getHash(),
                from.getWhen(),
                from.getOccurrence(),
                from.getSource(),
                from.getEvent(),
                from.getState(),
                LoggedItemSeverity.valueOf(from.getSeverity().name()),
                LoggedItemEventType.valueOf(from.getEventType().name()),
                from.getUser(),
                from.getSummary()
        );
    }
}