package service.apis.v1.converters;

import com.papaya.cycleactivitylog.service.apis.v1.responses.PageResponse;
import com.papaya.cycleactivitylog.service.model.Page;
import org.springframework.core.convert.converter.Converter;


public class PageToPageResponseConverter<R> implements Converter<Page<R>, PageResponse<R>> {
    @Override
    public PageResponse<R> convert(Page<R> from) {
        return PageResponse.<R>builder()
                .page(from.getPage())
                .size(from.getSize())
                .totalNumberOfElements(from.getTotalNumberOfElements())
                .hasPrevious(from.isHasPrevious())
                .hasNext(from.isHasNext())
                .content(from.getContent())
                .build();
    }

}