package service.data;

import com.papaya.cycleactivitylog.service.model.LoggedItem;

public interface LoggedItemsChannel {

    Batch<LoggedItem> nextBatch();

}
