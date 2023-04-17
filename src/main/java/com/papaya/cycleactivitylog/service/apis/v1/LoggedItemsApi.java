package com.papaya.cycleactivitylog.service.apis.v1;

import com.papaya.cycleactivitylog.service.apis.errors.ApiError;
import com.papaya.cycleactivitylog.service.apis.v1.requests.Pageable;
import com.papaya.cycleactivitylog.service.apis.v1.requests.PeriodRequest;
import com.papaya.cycleactivitylog.service.apis.v1.responses.LoggedItemResponse;
import com.papaya.cycleactivitylog.service.apis.v1.responses.PageResponse;
import com.papaya.cycleactivitylog.service.apis.v1.validations.ValidPeriod;
import com.papaya.cycleactivitylog.service.model.LoggedItemEventType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/v1/logged-items")
@Validated

public interface LoggedItemsApi {

    @GetMapping(path = "searches/by-cycle", produces = "application/json")

    @Operation(
            summary = "Search logged items by cycle",
            description = "Search logged items by cycle. Supported filters: period, source, state, event type",
            tags = {"search"})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public PageResponse<LoggedItemResponse> searchByCycle(
            @Parameter(description = "Id of the requested cycle")
            @RequestParam(name = "cycle-id") @NotEmpty String cycleId,

            @Parameter(description = "Sources filter")
            @RequestParam(required = false, defaultValue = "", name = "sources") List<String> sources,

            @Parameter(description = "States filter")
            @RequestParam(required = false, defaultValue = "", name = "states") List<String> states,

            @Parameter(description = "Event Types filter")
            @RequestParam(required = false, defaultValue = "", name = "event-types") List<LoggedItemEventType> eventTypes,

            @Parameter(description = "Period filter")
            @Valid @ValidPeriod @Nullable PeriodRequest period,

            @Parameter(description = "Requested page")
            @Valid Pageable pageable
    );

}
