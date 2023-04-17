package service.apis.services;

import com.papaya.cycleactivitylog.service.model.LoggedItem;
import com.papaya.cycleactivitylog.service.model.LoggedItemsSearchCriteria;
import com.papaya.cycleactivitylog.service.model.Page;

public interface LoggedItemsApiService {

    Page<LoggedItem> search(
            LoggedItemsSearchCriteria searchCriteria,
            int page,
            int size
    );


}
