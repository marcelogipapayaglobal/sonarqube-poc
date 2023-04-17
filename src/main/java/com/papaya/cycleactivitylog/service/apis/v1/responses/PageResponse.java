package com.papaya.cycleactivitylog.service.apis.v1.responses;


import com.papaya.cycleactivitylog.service.model.Page;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
public class PageResponse<T> {

    private final int page;

    private final int size;

    private final int totalNumberOfElements;

    private final boolean hasNext;

    private final boolean hasPrevious;

    private final List<T> content = new ArrayList<T>();

    @Builder
    public PageResponse(List<T> content, int page, int size, int totalNumberOfElements, boolean hasNext, boolean hasPrevious) {
        this.page = page;
        this.size = size;
        this.totalNumberOfElements = totalNumberOfElements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.content.addAll(content);
    }

    public <R> PageResponse(Page<R> page, Function<R, T> mapper) {
        this(
                page.getContent().stream().map(mapper).toList(),
                page.getPage(),
                page.getSize(),
                page.getTotalNumberOfElements(),
                page.isHasNext(),
                page.isHasPrevious()
        );
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

}

