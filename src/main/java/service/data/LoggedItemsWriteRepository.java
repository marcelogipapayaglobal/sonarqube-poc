package service.data;

import com.papaya.cycleactivitylog.service.model.LoggedItem;
import jakarta.validation.constraints.NotNull;

public interface LoggedItemsWriteRepository {

    void save(@NotNull Batch<LoggedItem> batch);
}
