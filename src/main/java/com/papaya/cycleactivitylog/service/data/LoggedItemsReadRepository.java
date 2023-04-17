package com.papaya.cycleactivitylog.service.data;

import com.papaya.cycleactivitylog.service.model.LoggedItem;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface LoggedItemsReadRepository {

    List<LoggedItem> findByCycleId(@NotNull String cycleId);

}
