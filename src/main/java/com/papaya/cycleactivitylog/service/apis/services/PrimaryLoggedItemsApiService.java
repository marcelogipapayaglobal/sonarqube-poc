package com.papaya.cycleactivitylog.service.apis.services;

import com.papaya.cycleactivitylog.service.data.LoggedItemsReadRepository;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import com.papaya.cycleactivitylog.service.model.LoggedItemsSearchCriteria;
import com.papaya.cycleactivitylog.service.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class PrimaryLoggedItemsApiService implements LoggedItemsApiService {

    private final LoggedItemsReadRepository repository;

    @Autowired
    public PrimaryLoggedItemsApiService(LoggedItemsReadRepository repository) {
        this.repository = repository;
    }

    private static List<LoggedItem> filterLoggedItems(LoggedItemsSearchCriteria searchCriteria, List<LoggedItem> items) {
        var period = searchCriteria.getPeriod();
        var sources = searchCriteria.getSources();
        var states = searchCriteria.getStates();
        var eventTypes = searchCriteria.getEventTypes();

        return items.stream()
                .filter(item -> period.isOpen() || period.contains(item.getWhen()))
                .filter(item -> sources.isEmpty() || sources.contains(item.getSource()))
                .filter(item -> states.isEmpty() || states.contains(item.getState()))
                .filter(item -> eventTypes.isEmpty() || eventTypes.contains(item.getEventType()))
                .toList();
    }

    private static Page<LoggedItem> paginate(List<LoggedItem> items, int page, int size) {
        int totalNumberOfElements = items.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalNumberOfElements);
        List<LoggedItem> content = fromIndex > totalNumberOfElements || toIndex <= fromIndex
                ? List.of()
                : items.subList(fromIndex, toIndex);

        return Page.<LoggedItem>builder()
                .page(page)
                .size(size)
                .totalNumberOfElements(totalNumberOfElements)
                .hasPrevious(fromIndex > 0)
                .hasNext(toIndex < totalNumberOfElements)
                .content(content)
                .build();
    }

    @Override
    public Page<LoggedItem> search(LoggedItemsSearchCriteria searchCriteria, int page, int size) {
        var loggedItems = findLoggedItemsByCycleId(searchCriteria);

        log.info("[search] found {} items for criteria {}", loggedItems.size(), searchCriteria);

        var filteredLoggedItems = filterLoggedItems(
                searchCriteria,
                loggedItems
        );

        log.info("[search] filtered {} items for criteria {}", filteredLoggedItems.size(), searchCriteria);

        var pageOfItems = paginate(
                filteredLoggedItems,
                page,
                size
        );

        log.info("[search] returns page with {} items for page {} and size {}", pageOfItems.getNumberOfElements(), page, size);

        return pageOfItems;
    }

    private List<LoggedItem> findLoggedItemsByCycleId(LoggedItemsSearchCriteria searchCriteria) {
        var items = new ArrayList<>(this.repository.findByCycleId(searchCriteria.getCycleId()));
        items.sort(Comparator.comparing(LoggedItem::getWhen));
        return items;
    }

}
