package service.apis.v1.controllers;

import com.papaya.cycleactivitylog.service.apis.services.LoggedItemsApiService;
import com.papaya.cycleactivitylog.service.apis.v1.LoggedItemsApi;
import com.papaya.cycleactivitylog.service.apis.v1.converters.LoggedItemToLoggedItemResponseConverter;
import com.papaya.cycleactivitylog.service.apis.v1.requests.Pageable;
import com.papaya.cycleactivitylog.service.apis.v1.requests.PeriodRequest;
import com.papaya.cycleactivitylog.service.apis.v1.responses.LoggedItemResponse;
import com.papaya.cycleactivitylog.service.apis.v1.responses.PageResponse;
import com.papaya.cycleactivitylog.service.model.LoggedItemEventType;
import com.papaya.cycleactivitylog.service.model.LoggedItemsSearchCriteria;
import com.papaya.cycleactivitylog.service.model.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class LoggedItemsApiController implements LoggedItemsApi {

    private final LoggedItemsApiService service;

    @Autowired
    public LoggedItemsApiController(LoggedItemsApiService service) {
        this.service = service;
    }


    public PageResponse<LoggedItemResponse> searchByCycle(
            String cycleId,
            List<String> sources,
            List<String> states,
            List<LoggedItemEventType> eventTypes,
            PeriodRequest period,
            Pageable pageable
    ) {
        var searchCriteria = new LoggedItemsSearchCriteria(
                cycleId,
                Optional.ofNullable(period).map(p -> new Period(period.getFrom(), period.getTo())).orElse(null),
                sources,
                states,
                eventTypes
        );

        var page = this.service.search(searchCriteria, pageable.getPage(), pageable.getSize());

        return new PageResponse<>(
                page,
                LoggedItemToLoggedItemResponseConverter::convertTo
        );

    }

}
