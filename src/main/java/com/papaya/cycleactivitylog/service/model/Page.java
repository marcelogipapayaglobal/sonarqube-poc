package com.papaya.cycleactivitylog.service.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
public class Page<T> {

    private final int page;

    private final int size;

    private final int totalNumberOfElements;

    private final boolean hasNext;

    private final boolean hasPrevious;

    private final List<T> content = new ArrayList<T>();

    @Builder
    public Page(List<T> content, int page, int size, int totalNumberOfElements, boolean hasNext, boolean hasPrevious) {
        this.page = page;
        this.size = size;
        this.totalNumberOfElements = totalNumberOfElements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.content.addAll(content);
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

    public <R> Page<R> map(ConversionService conversionService, Class<R> targetType) {
        return map(content -> conversionService.convert(content, targetType));
    }

    public <R> Page<R> map(Function<T, R> mapper) {
        return new Page<R>(
                content.stream().map(mapper).toList(),
                this.page,
                this.size,
                this.totalNumberOfElements,
                this.hasNext,
                this.hasPrevious
        );
    }

    public <R> R convert(ConversionService conversionService, Class<R> targetType) {
        return conversionService.convert(this, targetType);
    }


}

