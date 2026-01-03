package com.techconnect.opportunity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRangeValid, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Method getStart = value.getClass().getMethod("startDate");
            Method getEnd = value.getClass().getMethod("endDate");
            Object s = getStart.invoke(value);
            Object e = getEnd.invoke(value);
            if (s == null || e == null) return true; // @NotNull should handle nulls
            if (!(s instanceof LocalDate) || !(e instanceof LocalDate)) return true;
            LocalDate start = (LocalDate) s;
            LocalDate end = (LocalDate) e;
            return end.isAfter(start);
        } catch (NoSuchMethodException nsme) {
            // try other method names if needed
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
