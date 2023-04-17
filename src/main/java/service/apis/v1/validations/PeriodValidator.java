package service.apis.v1.validations;


import com.papaya.cycleactivitylog.service.apis.v1.requests.PeriodRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeriodValidator implements ConstraintValidator<ValidPeriod, PeriodRequest> {

    @Override
    public void initialize(ValidPeriod extension) {
    }

    @Override
    public boolean isValid(PeriodRequest period, ConstraintValidatorContext context) {
        return period.getFrom() == null || period.getTo() == null || !period.getFrom().isAfter(period.getTo());
    }

}