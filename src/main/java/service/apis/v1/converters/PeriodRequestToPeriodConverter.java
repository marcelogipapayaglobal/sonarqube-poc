package service.apis.v1.converters;

import com.papaya.cycleactivitylog.service.apis.v1.requests.PeriodRequest;
import com.papaya.cycleactivitylog.service.model.Period;
import org.springframework.core.convert.converter.Converter;


public class PeriodRequestToPeriodConverter implements Converter<PeriodRequest, Period> {

    @Override
    public Period convert(PeriodRequest from) {
        return new Period(from.getFrom(), from.getTo());
    }
}