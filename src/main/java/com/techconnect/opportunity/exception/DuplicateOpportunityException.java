package com.techconnect.opportunity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateOpportunityException extends RuntimeException {
    public DuplicateOpportunityException(String message) {
        super(message);
    }
}
